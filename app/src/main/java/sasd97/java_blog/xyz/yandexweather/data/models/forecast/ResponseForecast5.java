package sasd97.java_blog.xyz.yandexweather.data.models.forecast;

import java.util.List;

import sasd97.java_blog.xyz.yandexweather.data.models.weather.ResponseWeather;

/**
 * Created by Maksim Sukhotski on 8/7/2017.
 */

public class ResponseForecast5 {
    private String cod;
    private List<ResponseWeather> list;

    public List<ResponseWeather> getForecasts() {
        return list;
    }
}
