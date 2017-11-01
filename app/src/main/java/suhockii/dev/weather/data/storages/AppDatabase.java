package suhockii.dev.weather.data.storages;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.domain.models.WeatherModel;

import static suhockii.dev.weather.domain.models.WeatherModel.WEATHER_TABLE;

@Database(entities = {Place.class, WeatherModel.class}, version = 2)
@TypeConverters(RoomTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "database.name";

    public abstract PlacesDao placesDao();
    public abstract WeatherDao weatherDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("delete from "+ WEATHER_TABLE);
        }
    };
}
