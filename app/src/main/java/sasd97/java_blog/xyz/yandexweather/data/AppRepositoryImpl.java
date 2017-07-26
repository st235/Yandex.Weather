package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.data.net.PlacesApi;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.Storage;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public final class AppRepositoryImpl implements AppRepository {

    private WeatherApi weatherApi;
    private PlacesApi placesApi;
    private Storage<String> cacheStorage;
    private Storage<String> prefsStorage;
    private Pair<String, String> apiKeys;

    public AppRepositoryImpl(@NonNull WeatherApi weatherApi,
                             @NonNull PlacesApi placesApi,
                             @NonNull Pair<String, String> apiKeys,
                             @NonNull Storage<String> cacheStorage,
                             @NonNull Storage<String> prefsStorage) {
        this.weatherApi = weatherApi;
        this.placesApi = placesApi;
        this.apiKeys = apiKeys;
        this.cacheStorage = cacheStorage;
        this.prefsStorage = prefsStorage;
    }

    @Override
    public Observable<WeatherModel> getWeather(@NonNull Place place) {
        return weatherApi
                .getWeather(place.getCoords().first, place.getCoords().second, apiKeys.first)
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
    public Observable<PlacesResponse> getPlaces(@NonNull String s) {
        return placesApi.getPlaces(s, apiKeys.second);
    }

    @Override
    public Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String s) {
        return placesApi.getPlaceDetails(s, apiKeys.second);
    }

    @Override
    public String getCachedWeather(@NonNull Place place) {
        return this.cacheStorage.getString(toFileName(place), null);
    }

    @Override
    public void saveWeatherToCache(@NonNull Place place, @NonNull String json) {
        cacheStorage.put(toFileName(place), json);
    }

    private String toFileName(@NonNull Place place) {
        return place.getName().replaceAll(" ", "_").replaceAll(",", "").toLowerCase();
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
    public Completable savePlace(@NonNull Place place) {
        return new CompletableFromAction(() -> prefsStorage.put(PLACE_PREFS_KEY, place));
    }

    @Override
    public Place getPlace() {
        String s = prefsStorage.getString(PLACE_PREFS_KEY, "");
        String[] objects = s.split(" ");
        if (objects.length < 3)  // "some_city_coord1_coord2".split("_").length >= 3
            return new Place("", new Pair<>(0.0, 0.0));
        String c1 = objects[objects.length - 2];
        String c2 = objects[objects.length - 1];
        return new Place(s.split(c1)[0], new Pair<>(Double.valueOf(c1), Double.valueOf(c2)));
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
}
