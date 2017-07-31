package sasd97.java_blog.xyz.yandexweather.repository;

import android.support.v4.util.Pair;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.AppRepositoryImpl;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.Clouds;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.Coordinates;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.ResponseWeather;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.SunsetAndSunrise;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.Weather;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.WeatherInfo;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.Wind;
import sasd97.java_blog.xyz.yandexweather.data.net.PlacesApi;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.CacheStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.PrefsStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.Storage;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sasd97.java_blog.xyz.yandexweather.data.AppRepository.PLACE_PREFS_KEY;
import static sasd97.java_blog.xyz.yandexweather.data.AppRepository.UNITS_PRESSURE_PREFS_KEY;
import static sasd97.java_blog.xyz.yandexweather.data.AppRepository.UNITS_SPEED_PREFS_KEY;
import static sasd97.java_blog.xyz.yandexweather.data.AppRepository.UNITS_TEMPERATURE_PREFS_KEY;
import static sasd97.java_blog.xyz.yandexweather.data.AppRepository.WEATHER_UPDATE_INTERVAL_PREFS_KEY;
import static sasd97.java_blog.xyz.yandexweather.data.AppRepositoryImpl.toFileName;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

@RunWith(JUnit4.class)
public class AppRepositoryTest {
    private WeatherApi weatherApi;
    private PlacesApi placesApi;
    private Storage<String> cacheStorage;
    private Storage<String> prefsStorage;
    private Pair<String, String> apiKeys;
    private AppRepository repo;

    @Before
    public void setup() {
        weatherApi = mock(WeatherApi.class);
        placesApi = mock(PlacesApi.class);
        cacheStorage = mock(CacheStorage.class);
        prefsStorage = mock(PrefsStorage.class);
        apiKeys = new Pair<>("weatherApiKey", "placesApiKey");
        repo = new AppRepositoryImpl(weatherApi, placesApi, apiKeys, cacheStorage, prefsStorage);
    }

    @Test
    public void getCachedWeather() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);
        repo.getCachedWeather(place);
        verify(cacheStorage, times(1)).getString(toFileName(place), null);
        verify(weatherApi, never()).getWeather(coords.first, coords.second, apiKeys.first);
    }

    @Test
    public void getPlaces() {
        String query = "Mos";
        PlacesResponse placesResponse = new PlacesResponse();
        when(placesApi.getPlaces(query, apiKeys.second)).thenReturn(Observable.just(placesResponse));
        repo.getPlaces(query)
                .test()
                .assertValue(placesResponse);
    }

    @Test
    public void getPlaceDetails() {
        String placeId = "placeId";
        PlaceDetailsResponse placeDetailsResponse = new PlaceDetailsResponse();
        when(placesApi.getPlaceDetails(placeId, apiKeys.second)).thenReturn(Observable.just(placeDetailsResponse));
        repo.getPlaceDetails(placeId)
                .test()
                .assertValue(placeDetailsResponse);
    }

    @Test
    public void getWeather() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Москва";
        Place place = new Place(placeName, coords);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather());
        ResponseWeather responseWeather = new ResponseWeather(0,
                new Coordinates(coords.first, coords.second), weathers, "", new WeatherInfo(), 0, new Wind(),
                new Clouds(), 0, new SunsetAndSunrise(), "", 0);

        when(weatherApi.getWeather(place.getCoords().first, place.getCoords().second, apiKeys.first))
                .thenReturn(Observable.just(responseWeather));
        repo.getWeather(place)
                .test()
                .assertComplete();
        verify(weatherApi, times(1)).getWeather(place.getCoords().first, place.getCoords().second, apiKeys.first);
    }

    @Test
    public void getPlace() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        Place place = new Place("Москва", coords);
        when(prefsStorage.getString(PLACE_PREFS_KEY, "")).thenReturn(place.toString());
        Place repoPlace = repo.getPlace();
        Assert.assertEquals(repoPlace.toString(), place.toString());
    }

    @Test
    public void getPressureUnits() {
        int pressureUnits = ConvertersConfig.PRESSURE_PASCAL;
        when(prefsStorage.getInteger(UNITS_PRESSURE_PREFS_KEY, 0)).thenReturn(pressureUnits);
        Assert.assertEquals(repo.getPressureUnits(), pressureUnits);
        verify(prefsStorage, times(1)).getInteger(UNITS_PRESSURE_PREFS_KEY, 0);
    }

    @Test
    public void getSpeedUnits() {
        int speedUnits = ConvertersConfig.SPEED_MS;
        when(prefsStorage.getInteger(UNITS_SPEED_PREFS_KEY, 0)).thenReturn(speedUnits);
        int repoSpeedUnits = repo.getSpeedUnits();
        Assert.assertEquals(repoSpeedUnits, speedUnits);
        verify(prefsStorage, times(1)).getInteger(UNITS_SPEED_PREFS_KEY, 0);
    }

    @Test
    public void getTemperatureUnits() {
        int temperatureUnits = ConvertersConfig.TEMPERATURE_CELSIUS;
        when(prefsStorage.getInteger(UNITS_TEMPERATURE_PREFS_KEY, 0)).thenReturn(temperatureUnits);
        int repoTemperatureUnits = repo.getTemperatureUnits();
        Assert.assertEquals(repoTemperatureUnits, temperatureUnits);
        verify(prefsStorage, times(1)).getInteger(UNITS_TEMPERATURE_PREFS_KEY, 0);
    }

    @Test
    public void saveWeatherUpdateInterval() {
        int interval = 10;
        repo.saveWeatherUpdateInterval(interval);
        verify(prefsStorage).put(WEATHER_UPDATE_INTERVAL_PREFS_KEY, interval);
    }

    @Test
    public void savePlace() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        Place place = new Place("Москва", coords);
        repo.savePlace(place).subscribe();
        verify(prefsStorage, times(1)).put(PLACE_PREFS_KEY, place);
    }

    @Test
    public void savePressureUnits() {
        int pressureUnits = ConvertersConfig.PRESSURE_PASCAL;
        repo.savePressureUnits(pressureUnits);
        verify(prefsStorage, times(1)).put(UNITS_PRESSURE_PREFS_KEY, pressureUnits);
    }

    @Test
    public void saveSpeedUnits() {
        int speedUnits = ConvertersConfig.SPEED_MS;
        repo.saveSpeedUnits(speedUnits);
        verify(prefsStorage, times(1)).put(UNITS_SPEED_PREFS_KEY, speedUnits);
    }

    @Test
    public void saveTemperatureUnits() {
        int temperatureUnits = ConvertersConfig.TEMPERATURE_CELSIUS;
        repo.saveTemperatureUnits(temperatureUnits);
        verify(prefsStorage, times(1)).put(UNITS_TEMPERATURE_PREFS_KEY, temperatureUnits);
    }

    @Test
    public void saveWeatherToCache() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        String json = "{ key: value }";
        Place place = new Place(placeName, coords);
        repo.saveWeatherToCache(place, json);
        verify(cacheStorage, times(1)).put(toFileName(place), json);
        verify(weatherApi, never()).getWeather(coords.first, coords.second, apiKeys.first);
    }
}
