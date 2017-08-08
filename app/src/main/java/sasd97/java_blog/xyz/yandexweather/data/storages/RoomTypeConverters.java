package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.TypeConverter;
import android.support.v4.util.Pair;

@SuppressWarnings("WeakerAccess") // for Room
public class RoomTypeConverters {
    @TypeConverter
    public static String pairToString(Pair<Double, Double> pair) {
        if (pair == null) return null;
        return String.valueOf(pair.first) + " " + String.valueOf(pair.second);
    }

    @TypeConverter
    public static Pair<Double, Double> stringToPair(String s) {
        if (s == null) return null;
        String[] ss = s.split(" ");
        return new Pair<>(Double.valueOf(ss[0]), Double.valueOf(ss[1]));
    }
}
