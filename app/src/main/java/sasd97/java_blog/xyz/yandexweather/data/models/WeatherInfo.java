
package sasd97.java_blog.xyz.yandexweather.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherInfo {

    @SerializedName("temp")
    @Expose
    private Double temp;

    @SerializedName("pressure")
    @Expose
    private Integer pressure;

    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @SerializedName("temp_min")
    @Expose
    private Double tempMin;

    @SerializedName("temp_max")
    @Expose
    private Double tempMax;

    public Double getTemp() {
        return temp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WeatherInfo{");
        sb.append("temp=").append(temp);
        sb.append(", pressure=").append(pressure);
        sb.append(", humidity=").append(humidity);
        sb.append(", tempMin=").append(tempMin);
        sb.append(", tempMax=").append(tempMax);
        sb.append('}');
        return sb.toString();
    }
}
