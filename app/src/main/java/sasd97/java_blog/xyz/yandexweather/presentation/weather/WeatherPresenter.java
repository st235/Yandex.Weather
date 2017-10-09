package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.HashRunnable;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

import static sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl.FORECAST_NOT_ADDED;
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
    private Disposable d;
    private CompletableSource cs;

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
        initData();
    }

    private void initData() {
        placesInteractor.getSavedLocationPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(new HashRunnable(this::initData, "initData")))
                .doOnSuccess(this::onSuccessPlaceLoad)
                .subscribe((place, throwable) -> {/*ignore*/});
    }

    private void onSuccessPlaceLoad(Place place) {
        getWeather(place);
        getForecast(place);
    }

    public void getWeather(Place place) {
        weatherInteractor.getWeather(place)
                .onErrorResumeNext(weatherNotAdded -> weatherNotAdded.getLocalizedMessage().equals(WEATHER_NOT_ADDED) ?
                        updateWeather(new HashRunnable(() -> this.getWeather(place), "getWeather")) : null)
                .filter(weatherModel -> weatherModel != null).toSingle()
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
    }

    private void getForecast(Place place) {
        weatherInteractor.getForecast(place)
                .onErrorResumeNext(forecastNotAdded -> forecastNotAdded.getLocalizedMessage().equals(FORECAST_NOT_ADDED) ?
                        updateForecast(new HashRunnable(() -> this.getForecast(place), "getForecast")) : null)
                .map(this::addSettings)
                .compose(schedulers.getIoToMainTransformerSingle())
                .subscribe(hashMapSettingsPair -> getViewState().showForecast(hashMapSettingsPair), Throwable::printStackTrace);
    }

    private Single<WeatherModel> updateWeather(HashRunnable runnable) {
        return placesInteractor.getSavedLocationPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(runnable))
                .flatMap(weatherInteractor::updateWeather)
                .compose(schedulers.getIoToMainTransformerSingle());
    }

    private Single<Map<WeatherModel, WeatherType[]>> updateForecast(HashRunnable runnable) {
        return placesInteractor.getSavedLocationPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(runnable))
                .flatMap(weatherInteractor::updateForecast16)
                .zipWith(placesInteractor.getSavedLocationPlace()
                        .onErrorResumeNext(locationNotAdded -> updateLocationPlace(runnable))
                        .flatMap(weatherInteractor::updateForecast5), this::zipWithWeatherTypes)
                .flatMap(weatherInteractor::saveForecast)
                .compose(schedulers.getIoToMainTransformerSingle());
    }

    @SuppressWarnings({"ResourceType"})
    private Single<Place> updateLocationPlace(HashRunnable callingMethod) {
        return placesInteractor.getCurrentCoords()
                .flatMap(placesInteractor::getPlaceDetailsByCoords)
                .compose(schedulers.getIoToMainTransformerSingle())
                .map(placeDetails -> new Place(placeDetails.getCity(), placeDetails.getCoords()))
                .doOnSuccess(placesInteractor::savePlace)
                .doOnError(gpsDenied -> getViewState().requestEnablingGps(callingMethod))
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Fills weatherModelTypesHashMap with values.
     * 5 day forecast with interval 3 hour`s. Get for morning, day, evening, night.
     */
    @NonNull
    private LinkedHashMap<WeatherModel, WeatherType[]> zipWithWeatherTypes(LinkedHashMap<WeatherModel,
            WeatherType[]> weatherModelTypesHashMap, List<WeatherType> weatherTypes) {
        int currentDay = 0;
        for (WeatherType[] types : weatherModelTypesHashMap.values()) {
            for (int j = 0; j < 8; j += 2) {
                int index = j + 8 * currentDay;
                if (index >= weatherTypes.size()) break;
                types[j / 2] = weatherTypes.get(index);
            }
            currentDay++;
        }
        return weatherModelTypesHashMap;
    }

    private Pair<Map<WeatherModel, WeatherType[]>, Settings> addSettings(
            Map<WeatherModel, WeatherType[]> forecasts) {
        return new Pair<>(forecasts,
                new Settings(weatherInteractor.getTemperatureUnits(),
                        weatherInteractor.getSpeedUnits(),
                        weatherInteractor.getPressureUnits()));
    }

    public void fetchWeather() {
        updateWeather(new HashRunnable(this::fetchWeather, "fetchWeather"))
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
    }

    public void fetchForecast() {
        updateForecast(new HashRunnable(this::fetchForecast, "fetchForecast"))
                .map(this::addSettings)
                .doOnEvent((hashMapSettingsPair, throwable) -> getViewState().showForecast(hashMapSettingsPair))
                .zipWith(placesInteractor.getSavedLocationPlace(), Pair::new)
                .map(pairOfPairAndPlace -> {
                    pairOfPairAndPlace.first.first.keySet().forEach(weatherModel ->
                            weatherModel.setPlaceId(pairOfPairAndPlace.second.getPlaceId()));
                    return pairOfPairAndPlace.first.first.keySet();
                })
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .toList()
                .subscribe(weatherModels -> {}, Throwable::printStackTrace);
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
}
