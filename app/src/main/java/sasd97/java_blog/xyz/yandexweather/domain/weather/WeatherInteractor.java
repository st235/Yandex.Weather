package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherInteractor {
    Observable<ResponseWeather> getWeather(@NonNull String cityId, @NonNull String apiKey);
}
