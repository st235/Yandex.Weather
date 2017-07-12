package sasd97.java_blog.xyz.yandexweather.data.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherApi {

    @GET("/weather")
    Observable<ResponseWeather> getWeather(@Query("id") String cityId, @Query("appid") String apiKey);
}
