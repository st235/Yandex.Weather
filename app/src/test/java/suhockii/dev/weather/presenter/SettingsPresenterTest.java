package suhockii.dev.weather.presenter;

import org.junit.Before;
import org.junit.Test;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.converters.ConvertersConfig;
import suhockii.dev.weather.domain.settings.SettingsInteractor;
import suhockii.dev.weather.presentation.settings.SettingsPresenter;
import suhockii.dev.weather.presentation.settings.SettingsView;
import suhockii.dev.weather.utils.RxSchedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

public class SettingsPresenterTest {
    private SettingsView view;
    private SettingsPresenter presenter;
    private SettingsInteractor interactor;
    private RxSchedulers rxSchedulers;

    @Before
    public void setup() {
        view = mock(SettingsView.class);
        rxSchedulers = mock(RxSchedulers.class);
        interactor = mock(SettingsInteractor.class);
        presenter = new SettingsPresenter(rxSchedulers, interactor);
        presenter.attachView(view);
    }

    @Test
    public void getCurrentInterval() {
        presenter.getCurrentInterval();
        verify(interactor, only()).getUpdateInterval();
    }

    @Test
    public void isCelsius() {
        presenter.isCelsius();
        verify(interactor, only()).getTemperatureUnits();
    }

    @Test
    public void isMmHg() {
        presenter.isMmHg();
        verify(interactor, only()).getPressureUnits();
    }

    @Test
    public void isMs() {
        presenter.isMs();
        verify(interactor, only()).getSpeedUnits();
    }

    @Test
    public void isServiceEnabled() {
        presenter.isServiceEnabled();
        verify(interactor, only()).isBackgroundServiceEnabled();
    }

    @Test
    public void savePressure() {
        int unitsType = R.id.fragment_settings_pressure_mmhg;
        presenter.savePressure(unitsType);
        verify(interactor, only()).savePressureUnits(ConvertersConfig.PRESSURE_MMHG);
        verify(interactor, never()).savePressureUnits(ConvertersConfig.PRESSURE_PASCAL);
    }

    @Test
    public void saveSpeed() {
        int unitsType = R.id.fragment_settings_speed_ms;
        presenter.saveSpeed(unitsType);
        verify(interactor, only()).saveSpeedUnits(ConvertersConfig.SPEED_MS);
        verify(interactor, never()).saveSpeedUnits(ConvertersConfig.SPEED_KMH);
    }

    @Test
    public void saveTemperature() {
        int unitsType = R.id.fragment_settings_temperature_celsius;
        presenter.saveTemperature(unitsType);
        verify(interactor, only()).saveTemperatureUnits(ConvertersConfig.TEMPERATURE_CELSIUS);
        verify(interactor, never()).saveTemperatureUnits(ConvertersConfig.TEMPERATURE_FAHRENHEIT);
    }
}