package suhockii.dev.weather.data.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

public class LatLng {
    public static final String COMMA = ",";

    @SerializedName("lng")
    @Expose
    Double lng;

    @SerializedName("lat")
    @Expose
    Double lat;

    public LatLng(Double lat, Double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return lat + COMMA + lng;
    }
}
