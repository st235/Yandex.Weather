package suhockii.dev.weather.data.models.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    private float speed;

    @SerializedName("deg")
    @Expose
    private float degrees;

    public float getSpeed() {
        return speed;
    }

    public float getDegrees() {
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
