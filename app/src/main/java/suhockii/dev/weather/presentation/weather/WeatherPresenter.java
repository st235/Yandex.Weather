package suhockii.dev.weather.presentation.weather;

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
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.di.scopes.MainScope;
import suhockii.dev.weather.domain.converters.ConvertersConfig;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.domain.weather.WeatherInteractor;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import suhockii.dev.weather.utils.RxSchedulers;
import suhockii.dev.weather.utils.Settings;

import static suhockii.dev.weather.data.location.LocationProvider.GPS_DISABLED;
import static suhockii.dev.weather.domain.weather.WeatherInteractorImpl.FORECAST_NOT_ADDED;
import static suhockii.dev.weather.domain.weather.WeatherInteractorImpl.WEATHER_NOT_ADDED;
import static suhockii.dev.weather.domain.weather.WeatherInteractorImpl.addWeatherType;

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
        updateContent();
    }

    void updateContent() {
        placesInteractor.getCurrentPlace()
                .compose(schedulers.getIoToMainTransformerSingle())
                .doOnSubscribe(disposable -> getViewState().hideWeatherAndForecast())
                .compose(schedulers.getMainToIoTransformerSingle())
                .onErrorResumeNext(locationNotAdded -> updateLocationPlace(this::updateContent))
                .doOnSuccess(place -> {
                    getWeather(place);
                    getForecast(place, false);
                })
                .subscribe((place, throwable) -> {/*ignore*/});
    }

    private void getWeather(Place place) {
        weatherInteractor.getWeather(place)
                .compose(schedulers.getIoToMainTransformerSingle())
                .onErrorResumeNext(weatherNotAdded -> weatherNotAdded.getLocalizedMessage().equals(WEATHER_NOT_ADDED) ?
                        updateWeather(() -> this.getWeather(place)) : null)
                .filter(weatherModel -> weatherModel != null)
                .toSingle()
                .subscribe(this::chooseWeatherType, throwable -> getViewState().stopRefreshing());
    }

    void getForecast(Place place, boolean needUpdate) {
        weatherInteractor.getForecast(place, needUpdate)
                .onErrorResumeNext(forecastNotAdded -> forecastNotAdded.getLocalizedMessage().equals(FORECAST_NOT_ADDED) ?
                        updateForecast(() -> this.getForecast(place, needUpdate))
                                .compose(schedulers.getIoToMainTransformerSingle()) : null)
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .map(weatherModel -> addWeatherType(weatherModel, weatherInteractor.getWeatherTypes()))
                .collectInto(new LinkedHashMap<WeatherModel, WeatherType[]>(), (map, pair) ->
                        map.put(pair.first, pair.second))
                .map(this::addSettings)
                .compose(schedulers.getIoToMainTransformerSingle())
                .subscribe(hashMapSettingsPair -> getViewState().showForecast(hashMapSettingsPair),
                        throwable -> {
                            throwable.printStackTrace();
                            getViewState().stopRefreshing();
                        });
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
                        .zipWith(weatherInteractor.updateForecast5(place),
                                this::zipWithWeatherTypes)
                        .map(hashMap -> Pair.create(hashMap.keySet(), place)))
                .doOnSuccess(setAndPlace -> Observable.fromArray(setAndPlace.first
                        .toArray(new WeatherModel[setAndPlace.first.size()]))
                        .doOnNext(weatherModel -> weatherModel.setPlaceId(setAndPlace.second.getPlaceId()))
                        .subscribe())
                .map(pairOfMapAndPlace -> new ArrayList<>(pairOfMapAndPlace.first))
                .flatMap(weatherInteractor::saveForecast)
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .map(weatherModel -> {
                    WeatherModel weatherModelConv = weatherInteractor.convertModel(weatherModel);
                    return weatherModelConv;
                })
                .toList()
                .doOnSuccess(weatherModels -> {weatherModels.get(0).getForecastWeatherIds();});
    }

    @SuppressWarnings({"ResourceType"})
    private Single<Place> updateLocationPlace(Runnable callingMethod) {
        return placesInteractor.getCurrentCoords()
                .doOnSubscribe(ignore -> getViewState().showGpsSearch())
                .compose(schedulers.getMainToIoTransformerSingle())
                .flatMap(placesInteractor::getPlaceDetailsByCoords)
                .compose(schedulers.getIoToMainTransformerSingle())
                .map(placeDetails -> new Place(placeDetails.getCity(), placeDetails.getCoords(), placeDetails.getPlaceId()))
                .doOnSuccess(placesInteractor::updateCurrentPlace)
                .doOnSuccess(place -> getViewState().showPlaceName(place.getName()))
                .doOnError(gpsDisabled -> {
                    if (gpsDisabled.getMessage().contains("ACCESS_COARSE_LOCATION") ||
                            gpsDisabled.getMessage().contains("ACCESS_FINE_LOCATION") ||
                            gpsDisabled.getLocalizedMessage().equals(GPS_DISABLED)) {
                        getViewState().requestEnablingGps(callingMethod);
                    }
                })
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
                    Map<WeatherModel,
                    WeatherType[]> forecasts) {
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

    void updateByGps() {
        updateLocationPlace(this::updateByGps)
                .subscribe(place -> updateContent(), Throwable::printStackTrace);
    }
}
