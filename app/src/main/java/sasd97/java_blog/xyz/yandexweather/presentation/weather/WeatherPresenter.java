package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

/**
 * Created by alexander on 12/07/2017.
 */

@MainScope
@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private RxSchedulers schedulers;
    private PlacesInteractor placesInteractor;
    private WeatherInteractor weatherInteractor;
    private Set<WeatherType> weatherTypes;

    @Inject
    public WeatherPresenter(@NonNull RxSchedulers schedulers,
                            @NonNull Set<WeatherType> weatherTypes,
                            @NonNull PlacesInteractor placesInteractor,
                            @NonNull WeatherInteractor weatherInteractor) {
        this.placesInteractor = placesInteractor;
        this.weatherInteractor = weatherInteractor;
        this.schedulers = schedulers;
        this.weatherTypes = weatherTypes;
    }

    @Override
    public void attachView(WeatherView view) {
        super.attachView(view);
        Place place = placesInteractor.getPlace();
        weatherInteractor.getWeather(place)
                .compose(schedulers.getIoToMainTransformer())
                .map(weatherModel -> weatherModel.setCorrectCity(place))
                .subscribe(this::chooseWeather);
        getForecastFromDb();
    }

    public void getForecastFromDb() {
        weatherInteractor.getForecast(placesInteractor.getPlace().getPlaceId())
                .compose(schedulers.getComputationToMainTransformerSingle())
                .map(this::addSettings)
                .subscribe(getViewState()::showForecast);
    }

    @NonNull
    private LinkedHashMap<WeatherModel, WeatherType[]> zipWithWeatherTypes(
            LinkedHashMap<WeatherModel, WeatherType[]> hashMap, List<WeatherType> weatherTypes) {
        int currentDay = 0;
        for (WeatherType[] types : hashMap.values()) {
            /*5 day forecast with interval 3 hour`s. Get for morning, day, evening, night*/
            for (int j = 0; j < 8; j += 2) {
                int index = j + 8 * (currentDay);
                if (index >= weatherTypes.size()) break;
                types[j / 2] = weatherTypes.get(index);
            }
            currentDay++;
        }
        return hashMap;
    }

    private Pair<LinkedHashMap<WeatherModel, WeatherType[]>, Settings> addSettings(
            LinkedHashMap<WeatherModel, WeatherType[]> forecasts) {
        return new Pair<>(forecasts, getSettings());
    }

    public void fetchWeather() {
        weatherInteractor.updateWeather(placesInteractor.getPlace())
                .compose(schedulers.getIoToMainTransformer())
                .map(weatherModel -> weatherModel.setCorrectCity(placesInteractor.getPlace()))
                .subscribe(this::chooseWeather, Throwable::printStackTrace);
    }

    public void fetchForecast() {
        Place place = placesInteractor.getPlace();
        weatherInteractor.updateForecast16(place)
                .zipWith(weatherInteractor.updateForecast5(place), this::zipWithWeatherTypes)
                .compose(schedulers.getIoToMainTransformerSingle())
                .doOnSuccess(this::saveForecastToDb)
                .map(this::addSettings)
                .subscribe(getViewState()::showForecast);
    }

    private void saveForecastToDb(LinkedHashMap<WeatherModel, WeatherType[]> hashMap) {
        ArrayList<WeatherModel> forecast = new ArrayList<>(hashMap.keySet());
        forecast.forEach(weatherModel -> weatherModel.setPlaceId(placesInteractor.getPlace().getPlaceId()));
        weatherInteractor.saveForecast(forecast)
                .compose(schedulers.getIoToMainTransformerCompletable())
                .subscribe();
    }


    public boolean isCelsius() {
        return weatherInteractor.getTemperatureUnits() == ConvertersConfig.TEMPERATURE_CELSIUS;
    }

    public boolean isMs() {
        return weatherInteractor.getSpeedUnits() == ConvertersConfig.SPEED_MS;
    }

    public boolean isMmHg() {
        return weatherInteractor.getPressureUnits() == ConvertersConfig.PRESSURE_MMHG;
    }

    private void chooseWeather(@NonNull WeatherModel weather) {
        for (WeatherType type : weatherTypes) {
            if (!type.isApplicable(weather)) continue;
            getViewState().setWeather(weather, type);
            break;
        }

        getViewState().stopRefreshing();
    }

    public Settings getSettings() {
        return new Settings(weatherInteractor.getTemperatureUnits(),
                weatherInteractor.getSpeedUnits(),
                weatherInteractor.getPressureUnits());
    }
}
