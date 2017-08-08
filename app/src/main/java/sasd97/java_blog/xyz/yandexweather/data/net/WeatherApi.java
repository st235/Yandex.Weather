package sasd97.java_blog.xyz.yandexweather.data.net;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.ResponseWeather;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherApi {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("weather")
    Observable<ResponseWeather> getWeather(@Query("lat") Double lat,
                                           @Query("lon") Double lon,
                                           @Query("appid") String apiKey);

    @GET("forecast")
    Single<ResponseForecast> getForecast(@Query("lat") Double lat,
                                         @Query("lon") Double lon,
                                         @Query("appid") String apiKey);
}
