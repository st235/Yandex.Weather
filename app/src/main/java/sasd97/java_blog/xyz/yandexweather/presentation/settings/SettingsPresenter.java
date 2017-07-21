package sasd97.java_blog.xyz.yandexweather.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

/**
 * Created by alexander on 15/07/2017.
 */

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private RxSchedulers schedulers;
    private SettingsInteractor interactor;

    public SettingsPresenter(@NonNull RxSchedulers schedulers,
                             @NonNull SettingsInteractor interactor) {
        this.schedulers = schedulers;
        this.interactor = interactor;
    }

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);
        view.highlightSettings();
    }

    public void switchBackgroundServiceState() {
        boolean state = interactor.switchBackgroundServiceState();
        if (state) getViewState().showSwitcherGroup();
        else getViewState().hideSwitcherGroup();
    }

    public void saveTemperature(int id) {
        switch (id) {
            case R.id.fragment_settings_temperature_celsius:
                interactor.saveTemperatureUnits(ConvertersConfig.TEMPERATURE_CELSIUS);
                break;
            case R.id.fragment_settings_temperature_fahrenheit:
                interactor.saveTemperatureUnits(ConvertersConfig.TEMPERATURE_FAHRENHEIT);
                break;
        }
    }

    public void saveSpeed(int id) {
        switch (id) {
            case R.id.fragment_settings_speed_ms:
                interactor.saveSpeedUnits(ConvertersConfig.SPEED_MS);
                break;
            case R.id.fragment_settings_speed_kmh:
                interactor.saveSpeedUnits(ConvertersConfig.SPEED_KMH);
                break;
        }
    }

    public void savePressure(int id) {
        switch (id) {
            case R.id.fragment_settings_pressure_mmhg:
                interactor.savePressureUnits(ConvertersConfig.PRESSURE_MMHG);
                break;
            case R.id.fragment_settings_pressure_pascal:
                interactor.savePressureUnits(ConvertersConfig.PRESSURE_PASCAL);
                break;
        }
    }

    public int getCurrentInterval() {
        return interactor.getUpdateInterval();
    }

    public boolean isServiceEnabled() {
        return interactor.isBackgroundServiceEnabled();
    }

    public boolean isCelsius() {
        return interactor.getTemperatureUnits() == ConvertersConfig.TEMPERATURE_CELSIUS;
    }

    public boolean isMs() {
        return interactor.getSpeedUnits() == ConvertersConfig.SPEED_MS;
    }

    public boolean isMmHg() {
        return interactor.getPressureUnits() == ConvertersConfig.PRESSURE_MMHG;
    }
}
