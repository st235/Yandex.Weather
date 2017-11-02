package suhockii.dev.weather.data.storages;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import suhockii.dev.weather.data.models.places.Place;

import static suhockii.dev.weather.data.models.places.Place.PLACES_TABLE;

@Dao
public interface PlacesDao {
    @Query("SELECT * FROM " + PLACES_TABLE + " ORDER BY time DESC")
    Single<List<Place>> getPlaces();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(Place place);

    @Delete
    void removePlaces(List<Place> places);
}
