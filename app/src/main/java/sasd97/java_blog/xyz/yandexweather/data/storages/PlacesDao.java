package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

@Dao
public interface PlacesDao {
    @Query("SELECT * FROM Places ORDER BY time DESC")
    Single<List<Place>> getPlaces();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(Place place);

    @Delete
    void removePlaces(List<Place> places);
}
