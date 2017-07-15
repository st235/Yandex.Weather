package sasd97.java_blog.xyz.yandexweather.domain.settings;

/**
 * Created by alexander on 15/07/2017.
 */

public interface SettingsInteractor {
    void saveTemperatureUnits(int units);
    int getTemperatureUnits();

    void savePressureUnits(int units);
    int getPressureUnits();

    void saveSpeedUnits(int units);
    int getSpeedUnits();
}
