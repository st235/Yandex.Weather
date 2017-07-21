package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import java.util.Date;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.Storage;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public final class AppRepositoryImpl implements AppRepository {

    private WeatherApi weatherApi;
    private Storage<String> cacheStorage;
    private Storage<String> prefsStorage;

    public AppRepositoryImpl(@NonNull WeatherApi weatherApi,
                             @NonNull Storage<String> cacheStorage,
                             @NonNull Storage<String> prefsStorage) {
        this.weatherApi = weatherApi;
        this.cacheStorage = cacheStorage;
        this.prefsStorage = prefsStorage;
    }

    @Override
    public Observable<WeatherModel> getWeather(@NonNull String cityId) {
        return weatherApi
                .getWeather(cityId, WeatherApi.WEATHER_API_KEY)
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
    public String getCachedWeather(@NonNull String cityId) {
        return this.cacheStorage.getString(cityId, null);
    }

    @Override
    public void saveWeatherToCache(@NonNull String cityId, @NonNull String json) {
        cacheStorage.put(cityId, json);
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
    public void saveCity(@NonNull String cityId) {
        prefsStorage.put(CITY_PREFS_KEY, cityId);
    }

    @Override
    public String getCity() {
        return prefsStorage.getString(CITY_PREFS_KEY, "524901");
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
