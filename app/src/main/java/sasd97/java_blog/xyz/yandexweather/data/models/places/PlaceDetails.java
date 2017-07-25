package sasd97.java_blog.xyz.yandexweather.data.models.places;

import android.util.Pair;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

public class PlaceDetails {
    private Result result;
    private String status;

    public Pair<Double, Double> getLocation() {
        return new Pair<>(result.geometry.location.lat, result.geometry.location.lng);
    }
}
