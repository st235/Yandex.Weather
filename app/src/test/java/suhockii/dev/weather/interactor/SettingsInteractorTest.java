package suhockii.dev.weather.interactor;

import com.evernote.android.job.JobManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.domain.converters.ConvertersConfig;
import suhockii.dev.weather.domain.settings.SettingsInteractor;
import suhockii.dev.weather.domain.settings.SettingsInteractorImpl;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

@RunWith(JUnit4.class)
public class SettingsInteractorTest {
    @Mock
    private JobManager jobManager;
    private AppRepository repo;
    private SettingsInteractor settingsInteractor;

    @Before
    public void setup() {
        repo = mock(AppRepository.class);
        settingsInteractor = new SettingsInteractorImpl(jobManager, repo);
    }

    @Test
    public void getPressureUnits() {
        int units = ConvertersConfig.PRESSURE_MMHG;

        when(repo.getPressureUnits()).thenReturn(units);

        int unitsFromInteractor = settingsInteractor.getPressureUnits();
        assertEquals(units, unitsFromInteractor);
    }

    @Test
    public void getSpeedUnits() {
        int units = ConvertersConfig.SPEED_KMH;

        when(repo.getSpeedUnits()).thenReturn(units);

        int unitsFromInteractor = settingsInteractor.getSpeedUnits();
        assertEquals(units, unitsFromInteractor);
    }

    @Test
    public void getTemperatureUnits() {
        int units = ConvertersConfig.TEMPERATURE_CELSIUS;

        when(repo.getTemperatureUnits()).thenReturn(units);

        int unitsFromInteractor = settingsInteractor.getTemperatureUnits();
        assertEquals(units, unitsFromInteractor);
    }

    @Test
    public void getUpdateInterval() {
        int interval = 10000;

        when(repo.getWeatherUpdateInterval()).thenReturn(interval);

        int intevalFromInteractor = settingsInteractor.getUpdateInterval();
        assertEquals(interval, intevalFromInteractor);
    }

    @Test
    public void saveTemperatureUnits() {
        int units = ConvertersConfig.TEMPERATURE_CELSIUS;

        settingsInteractor.saveTemperatureUnits(units);
        verify(repo, times(1)).saveTemperatureUnits(units);
    }

    @Test
    public void savePressureUnits() {
        int units = ConvertersConfig.PRESSURE_MMHG;

        settingsInteractor.savePressureUnits(units);
        verify(repo, times(1)).savePressureUnits(units);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void isBackgroundServiceEnabled() {
        boolean value = false;

        when(repo.isBackgroundServiceEnabled()).thenReturn(value);

        boolean valueFromInteractor = settingsInteractor.isBackgroundServiceEnabled();
        verify(repo, times(1)).isBackgroundServiceEnabled();
        assertEquals(valueFromInteractor, value);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void switchBackgroundServiceState() {
        int interval = 10000;
        boolean value = false;

        when(repo.switchBackgroundServiceState()).thenReturn(value);
        when(repo.getWeatherUpdateInterval()).thenReturn(interval);

        boolean valueFromInteractor = settingsInteractor.switchBackgroundServiceState();
        verify(repo, times(1)).switchBackgroundServiceState();
        assertEquals(valueFromInteractor, value);
    }
}
