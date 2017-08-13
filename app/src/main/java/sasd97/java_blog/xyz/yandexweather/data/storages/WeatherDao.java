package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

import static sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel.WEATHER;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM " + WEATHER + " WHERE placeId IN (:placeId) ORDER BY updateTime DESC")
    Single<List<WeatherModel>> getForecast(String placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecast(List<WeatherModel> forecast);

    @Query("DELETE FROM " + WEATHER + " WHERE placeId = (:placeId)")
    void removeForecast(String placeId);
}
