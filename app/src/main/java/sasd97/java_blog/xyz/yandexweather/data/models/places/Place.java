package sasd97.java_blog.xyz.yandexweather.data.models.places;

import android.util.Pair;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

public class Place {
    String name;
    Pair<Double, Double> coords;

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
