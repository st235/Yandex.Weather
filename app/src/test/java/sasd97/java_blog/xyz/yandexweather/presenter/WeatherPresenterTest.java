package sasd97.java_blog.xyz.yandexweather.presenter;

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.di.modules.WeatherTypesModule;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherView;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

public class WeatherPresenterTest {
    private WeatherView view;
    private WeatherPresenter presenter;
    private WeatherInteractor weatherInteractor;
    private PlacesInteractor placesInteractor;
    private RxSchedulers rxSchedulers;
    private WeatherType weatherType;

    @Before
    public void setup() {
        view = mock(WeatherView.class);
        rxSchedulers = mock(RxSchedulers.class);
        weatherInteractor = mock(WeatherInteractor.class);
        placesInteractor = mock(PlacesInteractor.class);
        WeatherTypesModule weatherTypesModule = new WeatherTypesModule();
        Set<WeatherType> types = new HashSet<>();
        types.add(weatherTypesModule.provideClearSky());
        types.add(weatherTypesModule.provideCloudy());
        types.add(weatherTypesModule.provideDrizzle());
        types.add(weatherTypesModule.provideFoggy());
        types.add(weatherTypesModule.provideRainy());
        types.add(weatherTypesModule.provideSnowy());
        types.add(weatherTypesModule.provideSunny());
        types.add(weatherTypesModule.provideThunder());
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);

        when(placesInteractor.getPlace()).thenReturn(place);
        WeatherModel weatherModel = new WeatherModel.Builder().build();
        when(weatherInteractor.getWeather(place)).thenReturn(Observable.just(weatherModel));
        when(rxSchedulers.getIoToMainTransformer()).thenReturn(objectObservable -> objectObservable);

        presenter = new WeatherPresenter(rxSchedulers, types, placesInteractor, weatherInteractor);
        presenter.attachView(view);
    }

    @Test
    public void fetchWeather() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);
        WeatherModel weatherModel = new WeatherModel.Builder().build();

        when(placesInteractor.getPlace()).thenReturn(place);
        when(weatherInteractor.updateWeather(place)).thenReturn(Observable.just(weatherModel));
        when(rxSchedulers.getIoToMainTransformer()).thenReturn(objectObservable -> objectObservable);

        presenter.fetchWeather();
        verify(view, times(2)).stopRefreshing();
    }

    @Test
    public void isMs() {
        when(weatherInteractor.getSpeedUnits()).thenReturn(ConvertersConfig.SPEED_MS);
        presenter.isMs();
        verify(weatherInteractor, times(1)).getSpeedUnits();
    }

    @Test
    public void isMmHg() {
        presenter.isMmHg();
        verify(weatherInteractor, times(1)).getPressureUnits();
    }

    @Test
    public void isCelsius() {
        presenter.isCelsius();
        verify(weatherInteractor, times(1)).getTemperatureUnits();
    }
}