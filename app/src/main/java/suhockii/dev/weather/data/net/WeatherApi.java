package suhockii.dev.weather.data.net;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import suhockii.dev.weather.data.models.forecast.ResponseForecast16;
import suhockii.dev.weather.data.models.forecast.ResponseForecast5;
import suhockii.dev.weather.data.models.weather.ResponseWeather;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherApi {
    String base_url = "http://api.openweathermap.org/data/2.5/";
    int days = 16;

    @GET("weather")
    Single<ResponseWeather> getWeather(@Query("lat") double lat,
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
