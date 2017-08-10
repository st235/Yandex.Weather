package sasd97.java_blog.xyz.yandexweather.data.models.forecast;

import java.util.List;

/**
 * Created by Maksim Sukhotski on 8/7/2017.
 */

public class ResponseForecast {
    private String cod;
    private List<WeatherForecast> list;

    public List<WeatherForecast> getForecasts() {
        return list;
    }
}
