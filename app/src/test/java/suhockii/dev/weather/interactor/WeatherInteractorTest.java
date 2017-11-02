package suhockii.dev.weather.interactor;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.di.modules.ConvertersModule;
import suhockii.dev.weather.di.modules.DataModule;
import suhockii.dev.weather.domain.converters.Converter;
import suhockii.dev.weather.domain.converters.ConvertersConfig;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.domain.weather.WeatherInteractor;
import suhockii.dev.weather.domain.weather.WeatherInteractorImpl;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static suhockii.dev.weather.domain.converters.ConvertersConfig.PRESSURE_CONVERTERS_KEY;
import static suhockii.dev.weather.domain.converters.ConvertersConfig.SPEED_CONVERTERS_KEY;
import static suhockii.dev.weather.domain.converters.ConvertersConfig.TEMPERATURE_CONVERTERS_KEY;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

@RunWith(JUnit4.class)
public class WeatherInteractorTest {
    private AppRepository repo;
    private Gson gson;
    private WeatherInteractor weatherInteractor;
    private Set<WeatherType> weatherTypes;

    @Before
    public void setup() {
        repo = mock(AppRepository.class);
        WeatherType weatherType0 = mock(WeatherType.class);
        WeatherType weatherType1 = mock(WeatherType.class);
        Set<WeatherType> weatherTypes = new HashSet<>(2);
        weatherTypes = new HashSet<WeatherType>(2);
        weatherTypes.add(weatherType0);
        weatherTypes.add(weatherType1);
        DataModule dataModule = new DataModule();
        ConvertersModule convertersModule = new ConvertersModule();
        gson = dataModule.provideGson();
        Map<String, List<Converter<Integer, Float>>> converters = new HashMap<>(3);
        converters.put(PRESSURE_CONVERTERS_KEY, convertersModule.providePressureConverters());
        converters.put(SPEED_CONVERTERS_KEY, convertersModule.provideSpeedConverters());
        converters.put(TEMPERATURE_CONVERTERS_KEY, convertersModule.provideTemperatureConverters());
        weatherInteractor = new WeatherInteractorImpl(gson, repo, converters, weatherTypes);
    }

    @Test
    public void getCachedWeatherWhenExists() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");

        when(repo.getCachedWeather(place)).thenReturn("{\"coord\":{\"lon\":37.62,\"lat\":55.76},\"weather\":[{\"id\":200,\"main\":\"Thunderstorm\",\"description\":\"гроза с мелким дождём\",\"icon\":\"11n\"}],\"base\":\"stations\",\"name\":\"Moskovskaya Oblast’\"}");

        weatherInteractor.getWeather(place);
        verify(repo, times(1)).getCachedWeather(place);
        verify(repo, never()).getWeather(place);
    }

    @Test
    public void getCachedWeatherWhenNotExists() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");

        when(repo.getWeather(place)).thenReturn(Single.just(new WeatherModel.Builder().build()));
        when(repo.getCachedWeather(place)).thenReturn(null);

        weatherInteractor.getWeather(place);
        verify(repo, times(1)).getCachedWeather(place);
        verify(repo, times(1)).getWeather(place);
    }

    @Test
    public void updateWeather() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");

        WeatherModel fromRemote = new WeatherModel.Builder().build();

        when(repo.getWeather(place)).thenReturn(Single.just(fromRemote));

        weatherInteractor.updateWeather(place).test();
        verify(repo, never()).getCachedWeather(place);
        verify(repo, times(1)).getWeather(place);
        verify(repo, times(1)).saveWeatherToCache(place, gson.toJson(fromRemote));
    }

    @Test
    public void getSpeedUnits() {
        int units = ConvertersConfig.SPEED_KMH;
        when(repo.getSpeedUnits()).thenReturn(units);

        int interactorResponse = weatherInteractor.getSpeedUnits();
        verify(repo, times(1)).getSpeedUnits();
        assertTrue(units == interactorResponse);
    }

    @Test
    public void getPressureUnits() {
        int units = ConvertersConfig.PRESSURE_MMHG;
        when(repo.getSpeedUnits()).thenReturn(units);

        int interactorResponse = weatherInteractor.getPressureUnits();
        verify(repo, times(1)).getPressureUnits();
        assertTrue(units == interactorResponse);
    }

    @Test
    public void getTemperatureUnits() {
        int units = ConvertersConfig.TEMPERATURE_CELSIUS;

        when(repo.getSpeedUnits()).thenReturn(units);

        int unitsFromInteractor = weatherInteractor.getTemperatureUnits();
        verify(repo, times(1)).getTemperatureUnits();
        assertEquals(units, unitsFromInteractor);
    }
}
