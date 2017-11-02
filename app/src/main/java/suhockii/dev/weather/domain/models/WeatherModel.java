package suhockii.dev.weather.domain.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.TimeZone;

import suhockii.dev.weather.data.models.places.Place;

import static suhockii.dev.weather.WeatherApp.SPACE;

/**
 * Created by alexander on 14/07/2017.
 */

@Entity(tableName = "Weather")
public class WeatherModel {
    public static final String WEATHER_TABLE = "Weather";

    @Expose
    @PrimaryKey
    private int uid;

    @Expose
    private String placeId;

    @Expose
    private boolean isForecast;

    @Expose
    private int weatherId;

    @Expose
    private Integer[] forecastWeatherIds;

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

    public WeatherModel() {
    }

    private WeatherModel(@NonNull Builder builder) {
        this.isForecast = builder.isForecast;
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
        this.forecastWeatherIds = builder.forecastIds;
    }

    public void generateUid(int pos) {
        this.uid = placeId.hashCode() + pos;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setForecast(boolean forecast) {
        isForecast = forecast;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public Integer[] getForecastWeatherIds() {
        return forecastWeatherIds;
    }

    public void setForecastWeatherIds(Integer[] forecastWeatherIds) {
        this.forecastWeatherIds = forecastWeatherIds;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setNightTemperature(float nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public void setEveningTemperature(float eveningTemperature) {
        this.eveningTemperature = eveningTemperature;
    }

    public void setDayTemperature(float dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public void setMorningTemperature(float morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public void setWindDegree(float windDegree) {
        this.windDegree = windDegree;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public void setSunRiseTime(long sunRiseTime) {
        this.sunRiseTime = sunRiseTime;
    }

    public void setSunSetTime(long sunSetTime) {
        this.sunSetTime = sunSetTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setRainPercent(float rainPercent) {
        this.rainPercent = rainPercent;
    }

    public void setReadableDate(String readableDate) {
        this.readableDate = readableDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionLocalized(String descriptionLocalized) {
        this.descriptionLocalized = descriptionLocalized;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public boolean isForecast() {
        return isForecast;
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
            String[] strings = Instant.ofEpochSecond(updateTime)
                    .atZone(ZoneId.of(TimeZone.getDefault().getID()))
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)).split(SPACE);
            readableDate = (strings[0] + SPACE + strings[1]).replace(",", "");
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
        private boolean isForecast;
        private Integer[] forecastIds;

        public Builder() {
        }

        public Builder(@NonNull WeatherModel weather) {
            isForecast = weather.isForecast();
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
            forecastIds = weather.getForecastWeatherIds();
        }

        public Builder readableDate(String readableDate) {
            this.readableDate = readableDate;
            return this;
        }

        public Builder isForecast(boolean isForecast) {
            this.isForecast = isForecast;
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
