package sasd97.java_blog.xyz.yandexweather.data.models.places;

import android.support.v4.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

public class PlaceDetailsResponse {
    @SerializedName("result")
    @Expose
    private Result result;

    @SerializedName("status")
    @Expose
    private String status;

    public Pair<Double, Double> getCoords() {
        return new Pair<>(result.geometry.location.lat, result.geometry.location.lng);
    }
}
