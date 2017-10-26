package suhockii.dev.weather.data.models.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWeather {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("main")
    @Expose
    private WeatherInfo main;

    @SerializedName("visibility")
    @Expose
    private int visibility;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("dt")
    @Expose
    private int updateTime;

    @SerializedName("sys")
    @Expose
    private SunsetAndSunrise sunsetAndSunrise;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("cod")
    @Expose
    private int cod;

    public ResponseWeather(int id, Coordinates coordinates, List<Weather> weather, String base,
                           WeatherInfo main, int visibility, Wind wind, Clouds clouds,
                           int updateTime, SunsetAndSunrise sunsetAndSunrise, String name, int cod) {
        this.id = id;
        this.coordinates = coordinates;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.updateTime = updateTime;
        this.sunsetAndSunrise = sunsetAndSunrise;
        this.name = name;
        this.cod = cod;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public int getWeatherId() {
        return weather.get(0).getId();
    }

    public String getBase() {
        return base;
    }

    public WeatherInfo getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public SunsetAndSunrise getSunsetAndSunrise() {
        return sunsetAndSunrise;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResponseWeather{");
        sb.append("id=").append(id);
        sb.append(", coordinates=").append(coordinates);
        sb.append(", weather=").append(weather);
        sb.append(", base='").append(base).append('\'');
        sb.append(", main=").append(main);
        sb.append(", visibility=").append(visibility);
        sb.append(", wind=").append(wind);
        sb.append(", clouds=").append(clouds);
        sb.append(", dt=").append(updateTime);
        sb.append(", sunsetAndSunrise=").append(sunsetAndSunrise);
        sb.append(", name='").append(name).append('\'');
        sb.append(", cod=").append(cod);
        sb.append('}');
        return sb.toString();
    }
}
