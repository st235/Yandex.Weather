package sasd97.java_blog.xyz.yandexweather.data;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast16;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast5;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by alexander on 12/07/2017.
 */

public interface AppRepository {
    //constants
    String LOCATION_NOT_ADDED = "User location id not added.";
    String PLACE_PREFS_KEY = "app.prefs.place";
    String UNITS_TEMPERATURE_PREFS_KEY = "app.prefs.temperature.units";
    String UNITS_PRESSURE_PREFS_KEY = "app.prefs.pressure.units";
    String UNITS_SPEED_PREFS_KEY = "app.prefs.speed.units";
    String WEATHER_UPDATE_INTERVAL_PREFS_KEY = "app.prefs.weather.update.interval";
    String BACKGROUND_SERVICE_PREFS_KEY = "app.prefs.bg.service.state";

    //net
    Single<WeatherModel> getWeather(@NonNull Place place);
    Observable<ResponseForecast5> getForecast5(@NonNull Place place);
    Observable<ResponseForecast16> getForecast16(@NonNull Place place);
    Observable<PlacesResponse> getPlaces(@NonNull String s);
    Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String placeId);

    //db
    Single<List<Place>> getFavoritePlaces();
    Completable insertPlace(Place place);
    Completable removePlaces(List<Place> places);
    Single<List<WeatherModel>> getForecast(String placeId);
    Completable insertForecast(List<WeatherModel> forecast);
    Completable removeForecast(String placeId);

    //cache
    String getCachedWeather(@NonNull Place place);
    Completable saveWeatherToCache(@NonNull Place place, @NonNull String json);

    //prefs
    boolean isBackgroundServiceEnabled();
    boolean switchBackgroundServiceState();

    Completable savePlace(@NonNull Place place);
    Single<Place> getSavedLocationPlace();

    void saveWeatherUpdateInterval(int minutes);
    int getWeatherUpdateInterval();

    void saveTemperatureUnits(int units);
    int getTemperatureUnits();

    void savePressureUnits(int units);
    int getPressureUnits();

    void saveSpeedUnits(int units);
    int getSpeedUnits();

    @Nullable
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    Location getCurrentLocation();
}
