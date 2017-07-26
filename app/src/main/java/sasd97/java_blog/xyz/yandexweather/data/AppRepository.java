package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public interface AppRepository {
    //constants
    String PLACE_PREFS_KEY = "app.prefs.place";
    String UNITS_TEMPERATURE_PREFS_KEY = "app.prefs.temperature.units";
    String UNITS_PRESSURE_PREFS_KEY = "app.prefs.pressure.units";
    String UNITS_SPEED_PREFS_KEY = "app.prefs.speed.units";
    String WEATHER_UPDATE_INTERVAL_PREFS_KEY = "app.prefs.weather.update.interval";
    String BACKGROUND_SERVICE_PREFS_KEY = "app.prefs.bg.service.state";

    //net
    Observable<WeatherModel> getWeather(@NonNull Place place);
    Observable<PlacesResponse> getPlaces(@NonNull String s);
    Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String placeId);

    //cache
    String getCachedWeather(@NonNull Place place);
    void saveWeatherToCache(@NonNull Place place, @NonNull String json);

    //prefs
    boolean isBackgroundServiceEnabled();
    boolean switchBackgroundServiceState();

    Completable savePlace(@NonNull Place place);
    Place getPlace();

    void saveWeatherUpdateInterval(int minutes);
    int getWeatherUpdateInterval();

    void saveTemperatureUnits(int units);
    int getTemperatureUnits();

    void savePressureUnits(int units);
    int getPressureUnits();

    void saveSpeedUnits(int units);
    int getSpeedUnits();
}
