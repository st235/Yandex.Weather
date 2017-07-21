package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherInteractor {
    @NonNull String getCityId();

    Observable<WeatherModel> getWeather(@NonNull String cityId);
    Observable<WeatherModel> updateWeather(@NonNull String cityId);

    int getTemperatureUnits();
    int getPressureUnits();
    int getSpeedUnits();
}
