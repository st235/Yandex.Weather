
package suhockii.dev.weather.data.models.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SunsetAndSunrise {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("message")
    @Expose
    private double message;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("sunrise")
    @Expose
    private long sunriseTime;

    @SerializedName("sunset")
    @Expose
    private long sunsetTime;

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public double getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public long getSunriseTime() {
        return sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SunsetAndSunrise{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", message=").append(message);
        sb.append(", country='").append(country).append('\'');
        sb.append(", sunriseTime=").append(sunriseTime);
        sb.append(", sunsetTime=").append(sunsetTime);
        sb.append('}');
        return sb.toString();
    }
}
