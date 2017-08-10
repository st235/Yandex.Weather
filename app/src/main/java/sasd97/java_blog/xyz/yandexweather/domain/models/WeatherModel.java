package sasd97.java_blog.xyz.yandexweather.domain.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.TimeZone;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

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
    private float nightTemperature;

    @Expose
    private float eveningTemperature;

    @Expose
    private float dayTemperature;

    @Expose
    private float morningTemperature;

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

    @Expose
    private float rainPercent;

    @Expose
    private String readableDate;

    @Expose
    private String description;

    @Expose
    private String descriptionLocalized;

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
        this.nightTemperature = builder.nightTemperature;
        this.eveningTemperature = builder.eveningTemperature;
        this.dayTemperature = builder.dayTemperature;
        this.morningTemperature = builder.morningTemperature;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getCity() {
        return city;
    }

    public WeatherModel setCorrectCity(Place place) {
        String correct = place.getName().split(",")[0];
        this.city = correct.isEmpty() ? city : correct;
        return this;
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

    public String getReadableDate() {
        if (readableDate == null) {
            readableDate = Instant.ofEpochSecond(updateTime)
                    .atZone(ZoneId.of(TimeZone.getDefault().getID()))
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)).split(",")[0];
        }
        return readableDate;
    }

    public float getRainPercent() {
        return rainPercent;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionLocalized() {
        return descriptionLocalized;
    }

    public float getNightTemperature() {
        return nightTemperature;
    }

    public float getEveningTemperature() {
        return eveningTemperature;
    }

    public float getDayTemperature() {
        return dayTemperature;
    }

    public float getMorningTemperature() {
        return morningTemperature;
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
        private String readableDate;
        private float rainPercent;
        private String description;
        private String descriptionLocalized;
        private float nightTemperature;
        private float eveningTemperature;
        private float dayTemperature;
        private float morningTemperature;

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
            readableDate = weather.getReadableDate();
            rainPercent = weather.getRainPercent();
            description = weather.getDescription();
            descriptionLocalized = weather.getDescriptionLocalized();
            nightTemperature = weather.getNightTemperature();
            eveningTemperature = weather.getEveningTemperature();
            dayTemperature = weather.getDayTemperature();
            morningTemperature = weather.getMorningTemperature();
        }

        public Builder readableDate(String readableDate) {
            this.readableDate = readableDate;
            return this;
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

        public Builder rainPercent(float rainPercent) {
            this.rainPercent = rainPercent;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder descriptionLocalized(String descriptionLocalized) {
            this.descriptionLocalized = descriptionLocalized;
            return this;
        }

        public Builder nightTemperature(float nightTemperature) {
            this.nightTemperature = nightTemperature;
            return this;
        }

        public Builder eveningTemperature(float eveningTemperature) {
            this.eveningTemperature = eveningTemperature;
            return this;
        }

        public Builder dayTemperature(float dayTemperature) {
            this.dayTemperature = dayTemperature;
            return this;
        }

        public Builder morningTemperature(float morningTemperature) {
            this.morningTemperature = morningTemperature;
            return this;
        }

        public WeatherModel build() {
            return new WeatherModel(this);
        }
    }
}
