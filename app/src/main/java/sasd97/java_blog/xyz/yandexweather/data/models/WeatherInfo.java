
package sasd97.java_blog.xyz.yandexweather.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherInfo {

    @SerializedName("temp")
    @Expose
    private float temp;

    @SerializedName("pressure")
    @Expose
    private int pressure;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    @SerializedName("temp_min")
    @Expose
    private float tempMin;

    @SerializedName("temp_max")
    @Expose
    private float tempMax;

    public float getTemp() {
        return temp;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getTempMin() {
        return tempMin;
    }

    public float getTempMax() {
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
