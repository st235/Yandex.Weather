package sasd97.java_blog.xyz.yandexweather.domain.settings;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

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

    Completable savePlace(@NonNull Place place);
}
