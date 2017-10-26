package suhockii.dev.weather.repository;

import android.support.v4.util.Pair;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.data.AppRepositoryImpl;
import suhockii.dev.weather.data.location.LocationProvider;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.places.PlaceDetailsResponse;
import suhockii.dev.weather.data.models.places.PlacesResponse;
import suhockii.dev.weather.data.models.weather.Clouds;
import suhockii.dev.weather.data.models.weather.Coordinates;
import suhockii.dev.weather.data.models.weather.ResponseWeather;
import suhockii.dev.weather.data.models.weather.SunsetAndSunrise;
import suhockii.dev.weather.data.models.weather.Weather;
import suhockii.dev.weather.data.models.weather.WeatherInfo;
import suhockii.dev.weather.data.models.weather.Wind;
import suhockii.dev.weather.data.net.PlacesApi;
import suhockii.dev.weather.data.net.WeatherApi;
import suhockii.dev.weather.data.storages.CacheStorage;
import suhockii.dev.weather.data.storages.PlacesDao;
import suhockii.dev.weather.data.storages.PrefsStorage;
import suhockii.dev.weather.data.storages.Storage;
import suhockii.dev.weather.data.storages.WeatherDao;
import suhockii.dev.weather.domain.converters.ConvertersConfig;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static suhockii.dev.weather.data.AppRepository.PLACE_PREFS_KEY;
import static suhockii.dev.weather.data.AppRepository.UNITS_PRESSURE_PREFS_KEY;
import static suhockii.dev.weather.data.AppRepository.UNITS_SPEED_PREFS_KEY;
import static suhockii.dev.weather.data.AppRepository.UNITS_TEMPERATURE_PREFS_KEY;
import static suhockii.dev.weather.data.AppRepository.WEATHER_UPDATE_INTERVAL_PREFS_KEY;
import static suhockii.dev.weather.data.AppRepositoryImpl.toFileName;

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
    private PlacesDao placesDao;
    private WeatherDao weatherDao;
    private LocationProvider locationProvider;

    @Before
    public void setup() {
        weatherApi = mock(WeatherApi.class);
        placesApi = mock(PlacesApi.class);
        placesDao = mock(PlacesDao.class);
        weatherDao = mock(WeatherDao.class);
        cacheStorage = mock(CacheStorage.class);
        prefsStorage = mock(PrefsStorage.class);
        locationProvider = mock(LocationProvider.class);
        apiKeys = new Pair<>("weatherApiKey", "placesApiKey");
        repo = new AppRepositoryImpl(placesDao, weatherDao, weatherApi, placesApi, apiKeys,
                cacheStorage, prefsStorage, locationProvider);
    }

    @Test
    public void getCachedWeather() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");
        repo.getCachedWeather(place);
        verify(cacheStorage, times(1)).getString(toFileName(place), null);
        verify(weatherApi, never()).getWeather(coords.getLat(), coords.getLng(), apiKeys.first);
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
        when(placesApi.getPlaceDetailsById(placeId, apiKeys.second)).thenReturn(Observable.just(placeDetailsResponse));
        repo.getPlaceDetailsById(placeId)
                .test()
                .assertValue(placeDetailsResponse);
    }

    @Test
    public void getWeather() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Москва";
        Place place = new Place(placeName, coords, "");
        List<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather());
        ResponseWeather responseWeather = new ResponseWeather(0,
                new Coordinates(coords.getLat(), coords.getLng()), weathers, "", new WeatherInfo(), 0, new Wind(),
                new Clouds(), 0, new SunsetAndSunrise(), "", 0);

        when(weatherApi.getWeather(place.getCoords().getLat(), place.getCoords().getLng(), apiKeys.first))
                .thenReturn(Single.just(responseWeather));
        repo.getWeather(place)
                .test()
                .assertComplete();
        verify(weatherApi, times(1)).getWeather(place.getCoords().getLat(), place.getCoords().getLng(), apiKeys.first);
    }

    @Test
    public void getPlace() {
        Place place = new Place("ChIJybDUc_xKtUYRTM9XV8zWRD0", "Москва", new LatLng(0.0, 0.0), (int) Instant.now().getEpochSecond());
        when(prefsStorage.getString(PLACE_PREFS_KEY, "")).thenReturn(place.toString());
        String expected = "Москва *** 0.0 0.0 ChIJybDUc_xKtUYRTM9XV8zWRD0";
        Assert.assertEquals(expected, place.toString());
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
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        String json = "{ key: value }";
        Place place = new Place(placeName, coords, "");
        repo.saveWeatherToCache(place, json);
        verify(cacheStorage, times(1)).put(toFileName(place), json);
        verify(weatherApi, never()).getWeather(coords.getLat(), coords.getLng(), apiKeys.first);
    }
}
