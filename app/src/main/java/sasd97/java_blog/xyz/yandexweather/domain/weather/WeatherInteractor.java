package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherInteractor {
    Observable<WeatherModel> getWeather(@NonNull Place place);
    Observable<WeatherModel> updateWeather(@NonNull Place place);

    Single<ResponseForecast> getForecast(@NonNull Place place);
    Single<ResponseForecast> updateForecast(@NonNull Place place);

    int getTemperatureUnits();
    int getPressureUnits();
    int getSpeedUnits();
}
