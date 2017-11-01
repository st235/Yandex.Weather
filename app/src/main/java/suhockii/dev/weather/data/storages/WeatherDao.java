package suhockii.dev.weather.data.storages;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import suhockii.dev.weather.domain.models.WeatherModel;

import static suhockii.dev.weather.domain.models.WeatherModel.WEATHER_TABLE;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM " + WEATHER_TABLE + " WHERE placeId IN (:placeId) ORDER BY uid ASC")
    Single<List<WeatherModel>> getForecast(String placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecast(List<WeatherModel> forecast);

    @Query("DELETE FROM " + WEATHER_TABLE + " WHERE placeId = (:placeId)")
    void removeForecast(String placeId);
}
