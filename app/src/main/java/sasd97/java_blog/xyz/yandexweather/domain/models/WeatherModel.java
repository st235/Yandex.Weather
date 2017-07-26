package sasd97.java_blog.xyz.yandexweather.domain.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

/**
 * Created by alexander on 14/07/2017.
 */

public class WeatherModel {

    @Expose
    private int weatherId;

    @Expose
    private String city;

    @Expose
    private float humidity;

    @Expose
    private float pressure;

    @Expose
    private float temperature;

    @Expose
    private float minTemperature;

    @Expose
    private float maxTemperature;

    @Expose
    private float windDegree;

    @Expose
    private float windSpeed;

    @Expose
    private int clouds;

    @Expose
    private long sunRiseTime;

    @Expose
    private long sunSetTime;

    @Expose
    private long updateTime;

    private WeatherModel(@NonNull Builder builder) {
        this.weatherId = builder.weatherId;
        this.city = builder.city;
        this.humidity = builder.humidity;
        this.pressure = builder.pressure;
        this.temperature = builder.temperature;
        this.minTemperature = builder.minTemperature;
        this.maxTemperature = builder.maxTemperature;
        this.windDegree = builder.windDegree;
        this.windSpeed = builder.windSpeed;
        this.clouds = builder.clouds;
        this.sunRiseTime = builder.sunRiseTime;
        this.sunSetTime = builder.sunSetTime;
        this.updateTime = builder.updateTime;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getCity() {
        return city;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getWindDegree() {
        return windDegree;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public int getClouds() {
        return clouds;
    }

    public long getSunRiseTime() {
        return sunRiseTime;
    }

    public long getSunSetTime() {
        return sunSetTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WeatherModel{");
        sb.append("city='").append(city).append('\'');
        sb.append(", humidity='").append(humidity).append('\'');
        sb.append(", pressure='").append(pressure).append('\'');
        sb.append(", temperature=").append(temperature);
        sb.append(", minTemperature=").append(minTemperature);
        sb.append(", maxTemperature=").append(maxTemperature);
        sb.append(", windDegree=").append(windDegree);
        sb.append(", windSpeed=").append(windSpeed);
        sb.append(", clouds=").append(clouds);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {
        private int weatherId;
        private String city;
        private float humidity;
        private float pressure;
        private float temperature;
        private float minTemperature;
        private float maxTemperature;
        private float windDegree;
        private float windSpeed;
        private int clouds;
        private long sunRiseTime;
        private long sunSetTime;
        private long updateTime;

        public Builder() {
        }

        public Builder(@NonNull WeatherModel weather) {
            weatherId = weather.getWeatherId();
            city = weather.getCity();
            humidity = weather.getHumidity();
            pressure = weather.getPressure();
            temperature = weather.getTemperature();
            minTemperature = weather.getMinTemperature();
            maxTemperature = weather.getMaxTemperature();
            windDegree = weather.getWindDegree();
            windSpeed = weather.getWindSpeed();
            clouds = weather.getClouds();
            sunRiseTime = weather.getSunRiseTime();
            sunSetTime = weather.getSunSetTime();
            updateTime = weather.getUpdateTime();
        }

        public Builder weatherId(int weatherId) {
            this.weatherId = weatherId;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder humidity(float humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder pressure(float pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder minTemperature(float minTemperature) {
            this.minTemperature = minTemperature;
            return this;
        }

        public Builder maxTemperature(float maxTemperature) {
            this.maxTemperature = maxTemperature;
            return this;
        }

        public Builder windDegree(float windDegree) {
            this.windDegree = windDegree;
            return this;
        }

        public Builder windSpeed(float windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder clouds(int clouds) {
            this.clouds = clouds;
            return this;
        }

        public Builder sunRiseTime(long sunRiseTime) {
            this.sunRiseTime = sunRiseTime;
            return this;
        }

        public Builder sunSetTime(long sunSetTime) {
            this.sunSetTime = sunSetTime;
            return this;
        }

        public Builder updateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public WeatherModel build() {
            return new WeatherModel(this);
        }
    }
}
