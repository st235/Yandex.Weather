package sasd97.java_blog.xyz.yandexweather.data.models.places;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.v4.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import sasd97.java_blog.xyz.yandexweather.data.storages.RoomTypeConverters;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

@Entity(tableName = "Places")
@TypeConverters(RoomTypeConverters.class)
public class Place {
    @PrimaryKey
    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("coords")
    @Expose
    private Pair<Double, Double> coords;

    @Ignore
    public Place(String name, Pair<Double, Double> coords) {
        this.name = name;
        this.coords = coords;
    }

    public Place(String placeId, String name, Pair<Double, Double> coords) {
        this.placeId = placeId;
        this.name = name;
        this.coords = coords;
    }

    public String getPlaceId() {
        return placeId;
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
