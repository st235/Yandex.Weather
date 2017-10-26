package suhockii.dev.weather.domain.settings;

/**
 * Created by alexander on 15/07/2017.
 */

public interface SettingsInteractor {
    boolean isBackgroundServiceEnabled();
    boolean switchBackgroundServiceState();


    int getUpdateInterval();

    void saveTemperatureUnits(int units);
    int getTemperatureUnits();

    void savePressureUnits(int units);
    int getPressureUnits();

    void saveSpeedUnits(int units);
    int getSpeedUnits();
}
