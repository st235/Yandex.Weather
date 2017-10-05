package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

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

    private final RxSchedulers schedulers;
    private final PlacesInteractor placesInteractor;
    private final WeatherInteractor weatherInteractor;
    private final Set<WeatherType> weatherTypes;

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
        getWeather();

//         noinspection MissingPermission
//        placesInteractor.getUserLocationPlace()
//                .compose(schedulers.getIoToMainTransformerSingle())
//                .onErrorResumeNext(throwable -> placesInteractor.getCurrentLocation())
//                .flatMap(weatherInteractor::getForecast)
//                .compose(schedulers.getIoToMainTransformerSingle())
//                .map(this::addSettings)
//                .filter(hashMapSettingsPair -> {
//                    if (hashMapSettingsPair.first.keySet().isEmpty()) {
//                        fetchForecast();
//                        return false;
//                    }
//                    return true;
//                })
//                .subscribe(hashMapSettingsPair -> getViewState().showForecast(hashMapSettingsPair),
//                        Throwable::printStackTrace);
    }

    @SuppressWarnings({"ResourceType"})
    public void getWeather() {
        placesInteractor.getUserLocationPlace()
                .onErrorResumeNext(throwable -> placesInteractor.getCurrentLocation()
                        .doOnSuccess(placesInteractor::savePlace)
                        .doOnError(throwable1 -> getViewState().requestLocationPermissions(this::getWeather)))
                .flatMap(weatherInteractor::getWeather)
                .compose(schedulers.getIoToMainTransformerSingle())
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
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
        placesInteractor.getUserLocationPlace()
                .compose(schedulers.getIoToMainTransformerSingle())
                .flatMap(weatherInteractor::updateWeather)
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
    }

    public void fetchForecast() {
        placesInteractor.getUserLocationPlace()
                .compose(schedulers.getIoToMainTransformerSingle())
                .flatMap(weatherInteractor::updateForecast16)
                .zipWith(placesInteractor.getUserLocationPlace()
                        .flatMap(weatherInteractor::updateForecast5), this::zipWithWeatherTypes)
                .map(this::addSettings)
                .doOnEvent((hashMapSettingsPair, throwable) -> getViewState().showForecast(hashMapSettingsPair))
                .zipWith(placesInteractor.getUserLocationPlace(), Pair::new)
                .map(pairOfPairAndPlace -> {
                    Set<WeatherModel> weatherModelSet = pairOfPairAndPlace.first.first.keySet();
                    for (WeatherModel weather : weatherModelSet) {
                        weather.setPlaceId(pairOfPairAndPlace.second.getPlaceId());
                    }
                    return weatherModelSet;
                })
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .toList()
                .flatMapCompletable(weatherInteractor::saveForecast)
                .subscribe(() -> {}, Throwable::printStackTrace);
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

    private void chooseWeatherType(@NonNull WeatherModel weather) {
        for (WeatherType type : weatherTypes) {
            if (type.isApplicable(weather)) {
                getViewState().showWeather(weather, type);
                break;
            }
        }

        getViewState().stopRefreshing();
    }

    public Settings getSettings() {
        return new Settings(weatherInteractor.getTemperatureUnits(),
                weatherInteractor.getSpeedUnits(),
                weatherInteractor.getPressureUnits());
    }
}
