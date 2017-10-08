package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

import static sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl.WEATHER_NOT_ADDED;

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
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getWeather();
//        getForecast();
    }

    public void getWeather() {
        placesInteractor.getSavedLocationPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(this::getWeather))
                .onErrorReturn(throwable -> null)
                .filter(place -> place != null)
                .toSingle()
                .flatMap(weatherInteractor::getWeather)
                .onErrorResumeNext(weatherNotAdded -> weatherNotAdded.getLocalizedMessage().equals(WEATHER_NOT_ADDED) ? updateWeather() : null)
                .filter(weatherModel -> weatherModel != null)
                .toSingle()
                .compose(schedulers.getIoToMainTransformerSingle())
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
    }

    private Single<WeatherModel> updateWeather() {
        return placesInteractor.getSavedLocationPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(this::fetchWeather))
                .flatMap(weatherInteractor::updateWeather);
    }

    public void getForecast() {
        placesInteractor.getSavedLocationPlace()
                .onErrorResumeNext(throwable -> updateLocationPlace(this::getForecast))
                .flatMap(weatherInteractor::getForecast)
                .map(this::addSettings)
                .compose(schedulers.getIoToMainTransformerSingle())
                .filter(hashMapSettingsPair -> {
                    if (hashMapSettingsPair.first.keySet().isEmpty()) {
                        fetchForecast();
                        return false;
                    }
                    return true;
                })
                .subscribe(hashMapSettingsPair -> getViewState().showForecast(hashMapSettingsPair),
                        Throwable::printStackTrace);
    }

    @SuppressWarnings({"ResourceType"})
    private Single<Place> updateLocationPlace(Runnable callingMethod) {
        return placesInteractor.getCurrentCoords()
                .flatMap(placesInteractor::getPlaceDetailsByCoords)
                .map(placeDetails -> new Place(placeDetails.getCity(), placeDetails.getCoords()))
                .doOnSuccess(placesInteractor::savePlace)
                .doOnError(gpsDenied -> getViewState().requestEnablingGps(callingMethod))
                .subscribeOn(AndroidSchedulers.mainThread());
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
        updateWeather()
                .compose(schedulers.getIoToMainTransformerSingle())
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
    }

    public void fetchForecast() {
        placesInteractor.getSavedLocationPlace()
                .compose(schedulers.getIoToMainTransformerSingle())
                .flatMap(weatherInteractor::updateForecast16)
                .zipWith(placesInteractor.getSavedLocationPlace()
                        .flatMap(weatherInteractor::updateForecast5), this::zipWithWeatherTypes)
                .map(this::addSettings)
                .doOnEvent((hashMapSettingsPair, throwable) -> getViewState().showForecast(hashMapSettingsPair))
                .zipWith(placesInteractor.getSavedLocationPlace(), Pair::new)
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
