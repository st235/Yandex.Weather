package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public interface AppRepository {
    //constants
    String CITY_PREFS_KEY = "app.prefs.city";
    String UNITS_TEMPERATURE_PREFS_KEY = "app.prefs.temperature.units";
    String UNITS_PRESSURE_PREFS_KEY = "app.prefs.pressure.units";
    String UNITS_SPEED_PREFS_KEY = "app.prefs.speed.units";
    String WEATHER_UPDATE_INTERVAL_PREFS_KEY = "app.prefs.weather.update.interval";
    String BACKGROUND_SERVICE_PREFS_KEY = "app.prefs.bg.service.state";

    //net
    Observable<WeatherModel> getWeather(@NonNull String cityId);

    //cache
    String getCacheWeather(@NonNull String cityId);
    void saveWeatherToCache(@NonNull String cityId, @NonNull String json);

    //prefs
    boolean getBackgroundServiceMode();
    boolean switchBackgroundServiceMode();

    void saveCity(@NonNull String cityId);
    String getCity();

    void saveWeatherUpdateInterval(int minutes);
    int getWeatherUpdateInterval();

    void saveTemperatureUnits(int units);
    int getTemperatureUnits();

    void savePressureUnits(int units);
    int getPressureUnits();

    void saveSpeedUnits(int units);
    int getSpeedUnits();
}
