package suhockii.dev.weather.data;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import suhockii.dev.weather.data.models.forecast.ResponseForecast16;
import suhockii.dev.weather.data.models.forecast.ResponseForecast5;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.places.PlaceDetailsResponse;
import suhockii.dev.weather.data.models.places.PlacesResponse;
import suhockii.dev.weather.domain.models.WeatherModel;

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
    Observable<PlaceDetailsResponse> getPlaceDetailsById(@NonNull String placeId);
    Single<PlaceDetailsResponse> getPlaceDetailsByCoords(@NonNull LatLng latlng, @NonNull String lang);

    //db
    Single<List<Place>> getFavoritePlaces();
    Completable insertPlace(Place place);
    Completable removePlaces(List<Place> places);
    Maybe<List<WeatherModel>> getForecast(String placeId, boolean needUpdate);
    Completable insertForecast(List<WeatherModel> forecast);
    Completable removeForecast(String placeId);

    //cache
    String getCachedWeather(@NonNull Place place);
    Completable saveWeatherToCache(@NonNull Place place, @NonNull String json);

    //prefs
    boolean isBackgroundServiceEnabled();
    boolean switchBackgroundServiceState();

    void updateCurrentPlace(@NonNull Place place);
    Single<Place> getCurrentPlace();

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
    Location getCurrentLocation(SingleEmitter<LatLng> emitter);
}
