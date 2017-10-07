package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.arch.persistence.room.TypeConverter;

import sasd97.java_blog.xyz.yandexweather.data.models.places.LatLng;

import static sasd97.java_blog.xyz.yandexweather.data.models.places.LatLng.COMMA;

@SuppressWarnings("WeakerAccess") // for Room
public class RoomTypeConverters {
    @TypeConverter
    public static String LatLngToString(LatLng coords) {
        if (coords == null) {
            return null;
        }
        return coords.toString();
    }

    @TypeConverter
    public static LatLng stringToLatLng(String latLngString) {
        if (latLngString == null || !latLngString.contains(COMMA)) {
            return null;
        }
        String[] latLngStringParts = latLngString.split(COMMA);
        return new LatLng(Double.valueOf(latLngStringParts[0]),
                Double.valueOf(latLngStringParts[1]));
    }
}
