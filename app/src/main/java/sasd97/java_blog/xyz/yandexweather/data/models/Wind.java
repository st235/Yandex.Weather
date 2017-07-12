
package sasd97.java_blog.xyz.yandexweather.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    private Double speed;

    @SerializedName("deg")
    @Expose
    private Double degrees;

    public Double getSpeed() {
        return speed;
    }

    public Double getDegrees() {
        return degrees;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Wind{");
        sb.append("speed=").append(speed);
        sb.append(", degrees=").append(degrees);
        sb.append('}');
        return sb.toString();
    }
}
