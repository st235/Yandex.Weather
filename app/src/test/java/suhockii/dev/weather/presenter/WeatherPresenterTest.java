package suhockii.dev.weather.presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleFromCallable;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.di.modules.WeatherTypesModule;
import suhockii.dev.weather.domain.converters.ConvertersConfig;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.domain.weather.WeatherInteractor;
import suhockii.dev.weather.presentation.weather.WeatherPresenter;
import suhockii.dev.weather.presentation.weather.WeatherView;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import suhockii.dev.weather.utils.RxSchedulers;

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
    private boolean needUpdate = false;

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
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        String testId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";
        place = new Place(placeName, coords, "");
        place.setPlaceId(testId);
        when(placesInteractor.getCurrentPlace()).thenReturn(Single.just(place));
        WeatherModel weatherModel = new WeatherModel.Builder().build();
        when(weatherInteractor.getWeather(place)).thenReturn(Single.just(weatherModel));
        when(rxSchedulers.getIoToMainTransformerObservable()).thenReturn(objectObservable -> objectObservable);
        when(rxSchedulers.getIoToMainTransformerSingle()).thenReturn(objectObservable -> objectObservable);
        when(rxSchedulers.getComputationToMainTransformerSingle()).thenReturn(objectObservable -> objectObservable);
        when(rxSchedulers.getIoToMainTransformerCompletable()).thenReturn(objectObservable -> objectObservable);
        when(placesInteractor.getCurrentPlace()).thenReturn(Single.just(place));
        when(weatherInteractor.getForecast(place, needUpdate)).thenReturn(Single.fromCallable(LinkedList::new));
        when(weatherInteractor.updateForecast5(place)).thenReturn(Single.fromCallable(ArrayList::new));
        when(weatherInteractor.updateForecast16(place)).thenReturn(Single.fromCallable(LinkedHashMap::new));
        when(weatherInteractor.getPressureUnits()).thenReturn(ConvertersConfig.TEMPERATURE_CELSIUS);
        when(weatherInteractor.getTemperatureUnits()).thenReturn(ConvertersConfig.TEMPERATURE_CELSIUS);
        when(weatherInteractor.getSpeedUnits()).thenReturn(ConvertersConfig.SPEED_MS);

        List<WeatherModel> weatherModels = new LinkedList<>();
        when(weatherInteractor.saveForecast(weatherModels)).thenReturn(new SingleFromCallable<>(LinkedList::new));

        presenter = new WeatherPresenter(rxSchedulers, types, placesInteractor, weatherInteractor);
        presenter.attachView(view);
    }

    @Test
    public void fetchWeather() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");
        WeatherModel weatherModel = new WeatherModel.Builder().build();

        when(placesInteractor.getCurrentPlace()).thenReturn(Single.just(place));
        String testId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";
        when(weatherInteractor.getForecast(place, needUpdate)).thenReturn(Single.fromCallable(LinkedList::new));
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
        Map<WeatherModel, WeatherType[]> forecast = new LinkedHashMap<>();

//        presenter.fetchForecast();
        verify(weatherInteractor, times(3)).getTemperatureUnits();
        verify(weatherInteractor, times(2)).updateForecast16(place);
        verify(placesInteractor, times(6)).getCurrentPlace();
//        verify(weatherInteractor, times(2)).saveForecast(forecast);
    }
}