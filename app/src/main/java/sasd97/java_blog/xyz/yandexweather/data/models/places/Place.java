package sasd97.java_blog.xyz.yandexweather.data.models.places;

import android.support.v4.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

public class Place {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("coords")
    @Expose
    private Pair<Double, Double> coords;

    public Place(String name, Pair<Double, Double> coords) {
        this.name = name;
        this.coords = coords;
    }

    public String getName() {
        return name;
    }

    public Pair<Double, Double> getCoords() {
        return coords;
    }

    @Override
    public String toString() {
        return name + " " + coords.first.toString() + " " + coords.second.toString();
    }
}
