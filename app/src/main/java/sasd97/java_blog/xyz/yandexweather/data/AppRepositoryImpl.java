package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
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
    public Observable<ResponseBody> getWeather(@NonNull String cityId) {
        return weatherApi.getWeather(cityId, WeatherApi.WEATHER_API_KEY);
    }
}
