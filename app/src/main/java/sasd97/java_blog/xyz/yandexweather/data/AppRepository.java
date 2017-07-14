package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public interface AppRepository {
    String getCacheWeather(@NonNull String cityId);
    void saveWeather(@NonNull String cityId, @NonNull String json);
    Observable<WeatherModel> getWeather(@NonNull String cityId);
}
