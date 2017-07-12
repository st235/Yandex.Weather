
package sasd97.java_blog.xyz.yandexweather.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SunsetAndSunrise {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("message")
    @Expose
    private Double message;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("sunrise")
    @Expose
    private Integer sunriseTime;

    @SerializedName("sunset")
    @Expose
    private Integer sunsetTime;

    public Integer getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public Double getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public Integer getSunriseTime() {
        return sunriseTime;
    }

    public Integer getSunsetTime() {
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
