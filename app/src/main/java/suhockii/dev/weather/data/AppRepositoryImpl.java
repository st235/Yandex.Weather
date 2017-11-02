package suhockii.dev.weather.data;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import suhockii.dev.weather.data.location.LocationProvider;
import suhockii.dev.weather.data.models.forecast.ResponseForecast16;
import suhockii.dev.weather.data.models.forecast.ResponseForecast5;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.places.PlaceDetailsResponse;
import suhockii.dev.weather.data.models.places.PlacesResponse;
import suhockii.dev.weather.data.net.PlacesApi;
import suhockii.dev.weather.data.net.WeatherApi;
import suhockii.dev.weather.data.storages.PlacesDao;
import suhockii.dev.weather.data.storages.Storage;
import suhockii.dev.weather.data.storages.WeatherDao;
import suhockii.dev.weather.domain.models.WeatherModel;

import static suhockii.dev.weather.WeatherApp.SPACE;
import static suhockii.dev.weather.data.net.WeatherApi.days;
import static suhockii.dev.weather.domain.weather.WeatherInteractorImpl.FORECAST_NOT_ADDED;

/**
 * Created by alexander on 12/07/2017.
 */

public final class AppRepositoryImpl implements AppRepository {

    private WeatherApi weatherApi;
    private PlacesApi placesApi;
    private Storage<String> cacheStorage;
    private Storage<String> prefsStorage;
    private Pair<String, String> apiKeys;
    private PlacesDao placesDao;
    private WeatherDao weatherDao;
    private LocationProvider locationProvider;

    public AppRepositoryImpl(@NonNull PlacesDao placesDao,
                             @NonNull WeatherDao weatherDao,
                             @NonNull WeatherApi weatherApi,
                             @NonNull PlacesApi placesApi,
                             @NonNull Pair<String, String> apiKeys,
                             @NonNull Storage<String> cacheStorage,
                             @NonNull Storage<String> prefsStorage,
                             @NonNull LocationProvider locationProvider) {
        this.weatherDao = weatherDao;
        this.placesDao = placesDao;
        this.weatherApi = weatherApi;
        this.placesApi = placesApi;
        this.apiKeys = apiKeys;
        this.cacheStorage = cacheStorage;
        this.prefsStorage = prefsStorage;
        this.locationProvider = locationProvider;
    }

    @Override
    public Single<WeatherModel> getWeather(@NonNull Place place) {
        return weatherApi
                .getWeather(place.getCoords().getLat(), place.getCoords().getLng(), apiKeys.first)
                .map(w -> new WeatherModel.Builder()
                        .city(w.getName())
                        .weatherId(w.getWeather().get(0).getId())
                        .humidity(w.getMain().getHumidity())
                        .pressure(w.getMain().getPressure())
                        .temperature(w.getMain().getTemp())
                        .minTemperature(w.getMain().getTempMin())
                        .maxTemperature(w.getMain().getTempMax())
                        .windDegree(w.getWind().getDegrees())
                        .windSpeed(w.getWind().getSpeed())
                        .clouds(w.getClouds().getPercentile())
                        .sunRiseTime(w.getSunsetAndSunrise().getSunriseTime() * 1000)
                        .sunSetTime(w.getSunsetAndSunrise().getSunsetTime() * 1000)
                        .updateTime(new Date().getTime())
                        .build());
    }

    @Override
    public Observable<ResponseForecast5> getForecast5(@NonNull Place place) {
        return weatherApi.getForecast5(
                place.getCoords().getLat(), place.getCoords().getLng(), apiKeys.first);
    }

    @Override
    public Observable<ResponseForecast16> getForecast16(@NonNull Place place) {
        return weatherApi.getForecast16(
                place.getCoords().getLat(), place.getCoords().getLng(), days, apiKeys.first);
    }

    @Override
    public Observable<PlacesResponse> getPlaces(@NonNull String s) {
        return placesApi.getPlaces(s, apiKeys.second);
    }

    @Override
    public Observable<PlaceDetailsResponse> getPlaceDetailsById(@NonNull String placeId) {
        return placesApi.getPlaceDetailsById(placeId, apiKeys.second);
    }

    @Override
    public Single<PlaceDetailsResponse> getPlaceDetailsByCoords(@NonNull LatLng latlng,
                                                                @NonNull String lang) {
        return placesApi.getPlaceDetailsByCoords(latlng.toString(), lang, apiKeys.second);
    }

    @Override
    public Single<List<Place>> getFavoritePlaces() {
        return placesDao.getPlaces();
    }

    @Override
    public Completable insertPlace(Place place) {
        return Completable.fromAction(() -> placesDao.insertPlace(place));
    }

    @Override
    public Completable removePlaces(List<Place> places) {
        return new CompletableFromAction(() -> placesDao.removePlaces(places));
    }

    @Override
    public Maybe<List<WeatherModel>> getForecast(String placeId, boolean needUpdate) {
        return weatherDao.getForecast(placeId)
                .onErrorResumeNext(fileNotFound -> {
                    throw new NoSuchElementException(FORECAST_NOT_ADDED);
                })
                .filter(weatherModels -> {
                    if (weatherModels.isEmpty() || needUpdate) {
                        throw new NoSuchElementException(FORECAST_NOT_ADDED);
                    }
                    return true;
                });
    }

    @Override
    public Completable insertForecast(List<WeatherModel> forecast) {
        return Completable.fromAction(() -> weatherDao.insertForecast(forecast));
    }

    @Override
    public Completable removeForecast(String placeId) {
        return new CompletableFromAction(() -> weatherDao.removeForecast(placeId));
    }

    @Override
    public String getCachedWeather(@NonNull Place place) {
        return this.cacheStorage.getString(toFileName(place), null);
    }

    @Override
    public Completable saveWeatherToCache(@NonNull Place place, @NonNull String json) {
        return Completable.fromCallable(() -> cacheStorage.put(toFileName(place), json));
    }

    @Override
    public boolean isBackgroundServiceEnabled() {
        return prefsStorage.getBoolean(BACKGROUND_SERVICE_PREFS_KEY, true);
    }

    @Override
    public boolean switchBackgroundServiceState() {
        boolean mode = isBackgroundServiceEnabled();
        prefsStorage.put(BACKGROUND_SERVICE_PREFS_KEY, !mode);
        return !mode;
    }

    @Override
    public void updateCurrentPlace(@NonNull Place place) {
        prefsStorage.put(PLACE_PREFS_KEY, place);
    }

    @Override
    public Single<Place> getCurrentPlace() {
        return Single.fromCallable(() -> {
            String placeJson = prefsStorage.getString(PLACE_PREFS_KEY, null);
            if (placeJson == null) {
                throw new NoSuchElementException(LOCATION_NOT_ADDED);
            }
            return new Gson().fromJson(placeJson, Place.class);
        });
    }

    @Override
    public void saveWeatherUpdateInterval(int minutes) {
        prefsStorage.put(WEATHER_UPDATE_INTERVAL_PREFS_KEY, minutes);
    }

    @Override
    public int getWeatherUpdateInterval() {
        return prefsStorage.getInteger(WEATHER_UPDATE_INTERVAL_PREFS_KEY, 15);
    }

    @Override
    public void saveTemperatureUnits(int units) {
        prefsStorage.put(UNITS_TEMPERATURE_PREFS_KEY, units);
    }

    @Override
    public int getTemperatureUnits() {
        return prefsStorage.getInteger(UNITS_TEMPERATURE_PREFS_KEY, 0);
    }

    @Override
    public void savePressureUnits(int units) {
        prefsStorage.put(UNITS_PRESSURE_PREFS_KEY, units);
    }

    @Override
    public int getPressureUnits() {
        return prefsStorage.getInteger(UNITS_PRESSURE_PREFS_KEY, 0);
    }

    @Override
    public void saveSpeedUnits(int units) {
        prefsStorage.put(UNITS_SPEED_PREFS_KEY, units);
    }

    @Override
    public int getSpeedUnits() {
        return prefsStorage.getInteger(UNITS_SPEED_PREFS_KEY, 0);
    }

    @Nullable
    @Override
    public Location getCurrentLocation(SingleEmitter<LatLng> emitter) {
        // noinspection MissingPermission
        return locationProvider.getLastKnownLocation(emitter);
    }

    public static String toFileName(@NonNull Place place) {
        return place.getName().replaceAll(SPACE, "_").replaceAll(",", "").toLowerCase();
    }
}
