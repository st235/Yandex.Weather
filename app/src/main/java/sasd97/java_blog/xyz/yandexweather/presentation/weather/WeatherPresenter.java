package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;
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

import static sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl.FORECAST_NOT_ADDED;
import static sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl.WEATHER_NOT_ADDED;
import static sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl.addWeatherType;

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
        updateContent();
    }

    public void updateContent() {
        placesInteractor.getCurrentPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(this::updateContent))
                .doOnSuccess(place -> {
                    getWeather(place);
                    getForecast(place, false);
                })
                .subscribe((place, throwable) -> {/*ignore*/});
    }

    public void getWeather(Place place) {
        weatherInteractor.getWeather(place)
                .compose(schedulers.getIoToMainTransformerSingle())
                .onErrorResumeNext(weatherNotAdded -> weatherNotAdded.getLocalizedMessage().equals(WEATHER_NOT_ADDED) ?
                        updateWeather(() -> this.getWeather(place)) : null)
                .filter(weatherModel -> weatherModel != null).toSingle()
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
    }

    void getForecast(Place place, boolean needUpdate) {
        weatherInteractor.getForecast(place, needUpdate)
                .onErrorResumeNext(forecastNotAdded -> forecastNotAdded.getLocalizedMessage().equals(FORECAST_NOT_ADDED) ?
                        updateForecast(() -> this.getForecast(place, needUpdate))
                                .compose(schedulers.getIoToMainTransformerSingle()) : null)
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .map(weatherModel -> addWeatherType(weatherModel, weatherInteractor.getWeatherTypes()))
                .collectInto(new LinkedHashMap<WeatherModel, WeatherType[]>(), (map, pair) -> map.put(pair.first, pair.second))
                .map(this::addSettings)
                .compose(schedulers.getIoToMainTransformerSingle())
                .subscribe(hashMapSettingsPair -> getViewState().showForecast(hashMapSettingsPair), Throwable::printStackTrace);
    }

    private Single<WeatherModel> updateWeather(Runnable runnable) {
        return placesInteractor.getCurrentPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(runnable))
                .flatMap(weatherInteractor::updateWeather)
                .compose(schedulers.getIoToMainTransformerSingle());
    }

    private Single<List<WeatherModel>> updateForecast(Runnable runnable) {
        return placesInteractor.getCurrentPlace()
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(runnable))
                .flatMap(place -> weatherInteractor.updateForecast16(place)
                        .zipWith(weatherInteractor.updateForecast5(place), this::zipWithWeatherTypes)
                        .map(hashMap -> Pair.create(hashMap.keySet(), place)))
                .doOnSuccess(setAndPlace -> Observable.fromArray(setAndPlace.first
                        .toArray(new WeatherModel[setAndPlace.first.size()]))
                        .doOnNext(weatherModel -> weatherModel.setPlaceId(setAndPlace.second.getPlaceId()))
                        .subscribe())
                .map(pairOfMapAndPlace -> new ArrayList<>(pairOfMapAndPlace.first))
                .flatMap(weatherInteractor::saveForecast);
    }

    @SuppressWarnings({"ResourceType"})
    private Single<Place> updateLocationPlace(Runnable callingMethod) {
        return placesInteractor.getCurrentCoords()
                .flatMap(placesInteractor::getPlaceDetailsByCoords)
                .compose(schedulers.getIoToMainTransformerSingle())
                .map(placeDetails -> new Place(placeDetails.getCity(), placeDetails.getCoords(), placeDetails.getPlaceId()))
                .doOnSuccess(placesInteractor::updateCurrentPlace)
                .doOnError(gpsDenied -> getViewState().requestEnablingGps(callingMethod))
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Fills weatherModelTypesMap with values.
     * 5 day forecast with interval 3 hour`s. Get for morning, day, evening, night.
     */
    @NonNull
    private Map<WeatherModel, WeatherType[]> zipWithWeatherTypes(Map<WeatherModel, WeatherType[]> weatherModelTypesMap,
                                                                 List<WeatherType> weatherTypes) {
        int currentDay = 0;
        for (Map.Entry<WeatherModel, WeatherType[]> entry : weatherModelTypesMap.entrySet()) {
            for (int j = 0; j < 8; j += 2) {
                int index = j + 8 * currentDay;
                if (index >= weatherTypes.size()) {
                    break;
                }
                WeatherType[] value = entry.getValue();
                if (value.length - 1 < j / 2) {
                    WeatherType weatherType0 = value[0];
                    value = new WeatherType[5];
                    value[0] = weatherType0;
                }
                value[j / 2] = weatherTypes.get(index);
                if (entry.getKey().getForecastWeatherIds() == null) {
                    entry.getKey().setForecastWeatherIds(new Integer[4]);
                }
                entry.getKey().getForecastWeatherIds()[j / 2] = weatherTypes.get(index).getWeatherId();
            }
            currentDay++;
        }
        return weatherModelTypesMap;
    }

    private Pair<Map<WeatherModel, WeatherType[]>, Settings> addSettings(
            Map<WeatherModel, WeatherType[]> forecasts) {
        return new Pair<>(forecasts,
                new Settings(weatherInteractor.getTemperatureUnits(),
                        weatherInteractor.getSpeedUnits(),
                        weatherInteractor.getPressureUnits()));
    }

    public void fetchWeather() {
        updateWeather(this::fetchWeather)
                .subscribe(this::chooseWeatherType, Throwable::printStackTrace);
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
            if (type.isWeatherApplicable(weather)) {
                getViewState().showWeather(weather, type);
                break;
            }
        }

        getViewState().stopRefreshing();
    }
}
