package sasd97.java_blog.xyz.yandexweather.data.models.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordinates {

    @SerializedName("long")
    @Expose
    private double longitude;

    @SerializedName("lat")
    @Expose
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coordinates{");
        sb.append("longitude=").append(longitude);
        sb.append(", latitude=").append(latitude);
        sb.append('}');
        return sb.toString();
    }
}
