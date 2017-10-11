package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

@Database(entities = {Place.class, WeatherModel.class}, version = 1)
@TypeConverters(RoomTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "database.name";

    public abstract PlacesDao placesDao();
    public abstract WeatherDao weatherDao();
}
