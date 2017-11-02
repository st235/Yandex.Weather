package suhockii.dev.weather.data.models.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import suhockii.dev.weather.data.models.weather.Weather;
import suhockii.dev.weather.domain.models.WeatherModel;

public class WeatherForecast {

    @SerializedName("dt")
    @Expose
    private int updateTime;

    private String readableDate;

    @SerializedName("clouds")
    @Expose
    private int clouds;

    @SerializedName("humidity")
    @Expose
    private float humidity;

    @SerializedName("pressure")
    @Expose
    private float pressure;

    @SerializedName("speed")
    @Expose
    private float speed;

    @SerializedName("deg")
    @Expose
    private int deg;

    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    @SerializedName("temp")
    @Expose
    private Temperature temperature;

    @SerializedName("rain")
    @Expose
    private float rainPercent;

    public void setReadableDate(String readableDate) {
        this.readableDate = readableDate;
    }

    public String getReadableDate() {
        return readableDate;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public int getClouds() {
        return clouds;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public WeatherModel toWeatherModel() {
        return new WeatherModel.Builder()
                .isForecast(true)
                .weatherId(weather[0].getId())
                .clouds(clouds)
                .humidity(humidity)
                .maxTemperature(temperature.getMaximum())
                .minTemperature(temperature.getMinimum())
                .pressure(pressure)
                .readableDate(getReadableDate())
                .dayTemperature(temperature.getDay())
                .eveningTemperature(temperature.getEvening())
                .morningTemperature(temperature.getMorning())
                .nightTemperature(temperature.getNight())
                .rainPercent(rainPercent)
                .descriptionLocalized(weather[0].getDescription())
                .description(weather[0].getMain())
                .updateTime(updateTime)
                .build();
    }
}
