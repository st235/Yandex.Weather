package sasd97.java_blog.xyz.yandexweather.data.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherApi {

    String WEATHER_API_KEY = "38c3816a0d5664a2d18c447348f50f09";

    @GET("weather")
    Observable<ResponseWeather> getWeather(@Query("id") String cityId, @Query("appid") String apiKey);
}
