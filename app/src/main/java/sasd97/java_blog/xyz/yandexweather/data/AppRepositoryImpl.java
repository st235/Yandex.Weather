package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;

/**
 * Created by alexander on 12/07/2017.
 */

public class AppRepositoryImpl implements AppRepository {

    private WeatherApi weatherApi;

    public AppRepositoryImpl(@NonNull WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    @Override
    public Observable<ResponseWeather> getWeather(@NonNull String cityId, @NonNull String apiKey) {
        return weatherApi.getWeather(cityId, apiKey);
    }
}
