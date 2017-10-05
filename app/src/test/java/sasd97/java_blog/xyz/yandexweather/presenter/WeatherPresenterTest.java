package sasd97.java_blog.xyz.yandexweather.presenter;

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.internal.operators.completable.CompletableFromAction;
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
    Place place;

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
        String testId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";
        place = new Place(placeName, coords);
        place.setPlaceId(testId);
        when(placesInteractor.getUserLocationPlace()).thenReturn(Single.just(place));
        WeatherModel weatherModel = new WeatherModel.Builder().build();
        when(weatherInteractor.getWeather(place)).thenReturn(Single.just(weatherModel));
        when(rxSchedulers.getIoToMainTransformerObservable()).thenReturn(objectObservable -> objectObservable);
        when(rxSchedulers.getIoToMainTransformerSingle()).thenReturn(objectObservable -> objectObservable);
        when(rxSchedulers.getComputationToMainTransformerSingle()).thenReturn(objectObservable -> objectObservable);
        when(rxSchedulers.getIoToMainTransformerCompletable()).thenReturn(objectObservable -> objectObservable);
        when(placesInteractor.getUserLocationPlace()).thenReturn(Single.just(place));
        when(weatherInteractor.getForecast(place)).thenReturn(Single.fromCallable(LinkedHashMap::new));
        when(weatherInteractor.updateForecast5(place)).thenReturn(Single.fromCallable(ArrayList::new));
        when(weatherInteractor.updateForecast16(place)).thenReturn(Single.fromCallable(LinkedHashMap::new));
        when(weatherInteractor.getPressureUnits()).thenReturn(ConvertersConfig.TEMPERATURE_CELSIUS);
        when(weatherInteractor.getTemperatureUnits()).thenReturn(ConvertersConfig.TEMPERATURE_CELSIUS);
        when(weatherInteractor.getSpeedUnits()).thenReturn(ConvertersConfig.SPEED_MS);

        List<WeatherModel> weatherModels = new ArrayList<>();
        when(weatherInteractor.saveForecast(weatherModels)).thenReturn(new CompletableFromAction(() -> {
        }));

        presenter = new WeatherPresenter(rxSchedulers, types, placesInteractor, weatherInteractor);
        presenter.attachView(view);
    }

    @Test
    public void fetchWeather() {
        Pair<Double, Double> coords = new Pair<>(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords);
        WeatherModel weatherModel = new WeatherModel.Builder().build();

        when(placesInteractor.getUserLocationPlace()).thenReturn(Single.just(place));
        String testId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";
        when(weatherInteractor.getForecast(place)).thenReturn(Single.fromCallable(LinkedHashMap::new));
        when(weatherInteractor.updateWeather(place)).thenReturn(Single.just(weatherModel));

        presenter.fetchWeather();
        verify(view, times(2)).stopRefreshing();
    }

    @Test
    public void isMs() {
        presenter.isMs();
        verify(weatherInteractor, times(3)).getSpeedUnits();
    }

    @Test
    public void isMmHg() {
        when(weatherInteractor.getPressureUnits()).thenReturn(1);
        presenter.isMmHg();
        verify(weatherInteractor, times(3)).getPressureUnits();
    }

    @Test
    public void isCelsius() {
        presenter.isCelsius();
        verify(weatherInteractor, times(3)).getTemperatureUnits();
    }

    @Test
    public void fetchForecast() {
        ArrayList<WeatherModel> forecast = new ArrayList<>();

        presenter.fetchForecast();
        verify(weatherInteractor, times(3)).getTemperatureUnits();
        verify(weatherInteractor, times(2)).updateForecast16(place);
        verify(placesInteractor, times(6)).getUserLocationPlace();
        verify(weatherInteractor, times(2)).saveForecast(forecast);
    }
}