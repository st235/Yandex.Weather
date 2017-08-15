package sasd97.java_blog.xyz.yandexweather.data.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast16;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast5;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.ResponseWeather;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherApi {
    String base_url = "http://api.openweathermap.org/data/2.5/";
    int days = 16;

    @GET("weather")
    Observable<ResponseWeather> getWeather(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("appid") String apiKey);

    @GET("forecast")
    Observable<ResponseForecast5> getForecast5(@Query("lat") double lat,
                                               @Query("lon") double lon,
                                               @Query("appid") String apiKey);

    @GET("forecast/daily")
    Observable<ResponseForecast16> getForecast16(@Query("lat") double lat,
                                                 @Query("lon") double lon,
                                                 @Query("cnt") int cnt,
                                                 @Query("appid") String apiKey);
}
