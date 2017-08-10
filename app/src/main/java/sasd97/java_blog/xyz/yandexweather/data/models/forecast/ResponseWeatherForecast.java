package sasd97.java_blog.xyz.yandexweather.data.models.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWeatherForecast {

    @SerializedName("cnt")
    @Expose
    private int count;

    @SerializedName("cod")
    @Expose
    private int cod;

    @SerializedName("weather")
    @Expose
    private List<WeatherForecast> weather;

    @SerializedName("id")
    @Expose
    private int id;

}
