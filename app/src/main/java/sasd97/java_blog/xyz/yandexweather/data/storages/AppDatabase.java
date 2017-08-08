package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

@Database(entities = {Place.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "database.name";

    public abstract PlacesDao placesDao();
}
