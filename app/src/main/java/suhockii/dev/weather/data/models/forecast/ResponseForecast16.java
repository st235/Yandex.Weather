package suhockii.dev.weather.data.models.forecast;

import java.util.List;

/**
 * Created by Maksim Sukhotski on 8/7/2017.
 */

public class ResponseForecast16 {
    private String cod;
    private List<WeatherForecast> list;

    public List<WeatherForecast> getForecasts() {
        return list;
    }
}
