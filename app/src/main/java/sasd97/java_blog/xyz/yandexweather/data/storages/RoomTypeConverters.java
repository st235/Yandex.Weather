package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.TypeConverter;
import android.support.v4.util.Pair;

import static sasd97.java_blog.xyz.yandexweather.WeatherApp.SPACE;

@SuppressWarnings("WeakerAccess") // for Room
public class RoomTypeConverters {
    @TypeConverter
    public static String pairToString(Pair<Double, Double> pair) {
        if (pair == null) return null;
        return String.valueOf(pair.first) + SPACE + String.valueOf(pair.second);
    }

    @TypeConverter
    public static Pair<Double, Double> stringToPair(String s) {
        if (s == null) return null;
        String[] ss = s.split(SPACE);
        return new Pair<>(Double.valueOf(ss[0]), Double.valueOf(ss[1]));
    }
}
