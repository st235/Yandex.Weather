package sasd97.java_blog.xyz.yandexweather.interactor;

import android.support.v4.util.Pair;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.di.modules.ConvertersModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.DataModule;
import sasd97.java_blog.xyz.yandexweather.domain.converters.Converter;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.PRESSURE_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.SPEED_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.TEMPERATURE_CONVERTERS_KEY;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

@RunWith(JUnit4.class)
public class WeatherInteractorTest {
    private AppRepository repo;
    private Gson gson;
    private WeatherInteractor weatherInteractor;

    @Before
    public void setup() {
        repo = mock(AppRepository.class);
        DataModule dataModule = new DataModule();
        ConvertersModule convertersModule = new ConvertersModule();
        gson = dataModule.provideGson();
        Map<String, List<Converter<Integer, Float>>> converters = new HashMap<>(3);
        converters.put(PRESSURE_CONVERTERS_KEY, convertersModule.providePressureConverters());
        converters.put(SPEED_CONVERTERS_KEY, convertersModule.provideSpeedConverters());
        converters.put(TEMPERATURE_CONVERTERS_KEY, convertersModule.provideTemperatureConverters());
        weatherInteractor = new WeatherInteractorImpl(gson, repo, converters);
    }

    @Test
    public void getCachedWeatherWhenExists() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);

        when(repo.getCachedWeather(place)).thenReturn("{\"coord\":{\"lon\":37.62,\"lat\":55.76},\"weather\":[{\"id\":200,\"main\":\"Thunderstorm\",\"description\":\"гроза с мелким дождём\",\"icon\":\"11n\"}],\"base\":\"stations\",\"name\":\"Moskovskaya Oblast’\"}");

        weatherInteractor.getWeather(place);
        verify(repo, times(1)).getCachedWeather(place);
        verify(repo, never()).getWeather(place);
    }

    @Test
    public void getCachedWeatherWhenNotExists() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);

        when(repo.getWeather(place)).thenReturn(Observable.just(new WeatherModel.Builder().build()));
        when(repo.getCachedWeather(place)).thenReturn(null);

        weatherInteractor.getWeather(place);
        verify(repo, times(1)).getCachedWeather(place);
        verify(repo, times(1)).getWeather(place);
    }

    @Test
    public void updateWeather() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);

        WeatherModel fromRemote = new WeatherModel.Builder().build();

        when(repo.getWeather(place)).thenReturn(Observable.just(fromRemote));

        weatherInteractor.updateWeather(place).test();
        verify(repo, never()).getCachedWeather(place);
        verify(repo, times(1)).getWeather(place);
        verify(repo, times(1)).saveWeatherToCache(place, gson.toJson(fromRemote));
    }

    @Test
    public void getPlace() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);

        when(repo.getPlace()).thenReturn(place);

        weatherInteractor.getPlace();
        verify(repo, times(1)).getPlace();
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
