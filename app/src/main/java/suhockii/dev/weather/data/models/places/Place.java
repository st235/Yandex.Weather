package suhockii.dev.weather.data.models.places;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

@Entity(tableName = Place.PLACES_TABLE)
public class Place {
    public static final String PLACES_TABLE = "Places";

    @PrimaryKey
    @SerializedName("place_id")
    @Expose
    @NonNull
    private String placeId;

    @SerializedName("time")
    @Expose
    private int time;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("coords")
    @Expose
    private LatLng coords;

    public Place(String placeId, String name, LatLng coords, int time) {
        this.placeId = placeId;
        this.name = name;
        this.coords = coords;
        this.time = time;
    }

    @Ignore
    public Place() {}

    @Ignore
    public Place(String name, LatLng coords, String placeId) {
        this.name = name;
        this.coords = coords;
        this.placeId = placeId;
    }

    @Ignore
    public Place(String placeId, LatLng coords, int time) {
        this.placeId = placeId;
        this.coords = coords;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LatLng getCoords() {
        return coords;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
