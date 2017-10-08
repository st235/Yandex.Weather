package sasd97.java_blog.xyz.yandexweather.data.models.places;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import sasd97.java_blog.xyz.yandexweather.data.storages.RoomTypeConverters;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

@Entity(tableName = Place.PLACES_TABLE)
@TypeConverters(RoomTypeConverters.class)
public class Place {
    public static final String PLACES_TABLE = "Places";

    @PrimaryKey
    @SerializedName("place_id")
    @Expose
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
    public Place(String name, LatLng coords) {
        this.name = name;
        this.coords = coords;
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
