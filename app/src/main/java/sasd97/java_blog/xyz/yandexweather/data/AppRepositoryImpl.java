package sasd97.java_blog.xyz.yandexweather.data;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.CacheStorage;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public class AppRepositoryImpl implements AppRepository {

    private WeatherApi weatherApi;
    private CacheStorage cacheStorage;

    public AppRepositoryImpl(@NonNull WeatherApi weatherApi,
                             @NonNull CacheStorage cacheStorage) {
        this.weatherApi = weatherApi;
        this.cacheStorage = cacheStorage;
    }

    @Override
    public String getCacheWeather(@NonNull String cityId) {
        return this.cacheStorage.getString(cityId, null);
    }

    @Override
    public void saveWeather(@NonNull String cityId, @NonNull String json) {
        cacheStorage.put(cityId, json);
    }

    @Override
    public Observable<WeatherModel> getWeather(@NonNull String cityId) {
        return weatherApi
                .getWeather(cityId, WeatherApi.WEATHER_API_KEY)
                .map(w -> new WeatherModel.Builder()
                                .city(w.getName())
                                .weatherId(w.getWeather().get(0).getId())
                                .humidity(w.getMain().getHumidity())
                                .pressure(w.getMain().getPressure())
                                .temperature(w.getMain().getTemp())
                                .minTemperature(w.getMain().getTempMin())
                                .maxTemperature(w.getMain().getTempMax())
                                .windDegree(w.getWind().getDegrees())
                                .windSpeed(w.getWind().getSpeed())
                                .clouds(w.getClouds().getPercentile())
                                .sunRiseTime(w.getSunsetAndSunrise().getSunriseTime())
                                .sunSetTime(w.getSunsetAndSunrise().getSunsetTime())
                            .build());
    }
}
