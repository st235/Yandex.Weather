package sasd97.java_blog.xyz.yandexweather.domain.models;

import android.support.annotation.NonNull;

/**
 * Created by alexander on 14/07/2017.
 */

public class WeatherModel {

    private int weatherId;
    private String city;
    private int humidity;
    private int pressure;
    private double temperature;
    private double minTemperature;
    private double maxTemperature;
    private double windDegree;
    private double windSpeed;
    private int clouds;
    private long sunRiseTime;
    private long sunSetTime;

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
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getCity() {
        return city;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public double getWindSpeed() {
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
        private int humidity;
        private int pressure;
        private double temperature;
        private double minTemperature;
        private double maxTemperature;
        private double windDegree;
        private double windSpeed;
        private int clouds;
        private long sunRiseTime;
        private long sunSetTime;

        public Builder weatherId(int weatherId) {
            this.weatherId = weatherId;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder pressure(int pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder minTemperature(double minTemperature) {
            this.minTemperature = minTemperature;
            return this;
        }

        public Builder maxTemperature(double maxTemperature) {
            this.maxTemperature = maxTemperature;
            return this;
        }

        public Builder windDegree(double windDegree) {
            this.windDegree = windDegree;
            return this;
        }

        public Builder windSpeed(double windSpeed) {
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

        public WeatherModel build() {
            return new WeatherModel(this);
        }
    }
}
