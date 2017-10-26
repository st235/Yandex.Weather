package suhockii.dev.weather.data.storages;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import suhockii.dev.weather.data.models.places.LatLng;

import static suhockii.dev.weather.data.models.places.LatLng.COMMA;

@SuppressWarnings("WeakerAccess") // for Room
public class RoomTypeConverters {
    @TypeConverter
    public static String latLngToString(LatLng coords) {
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

    @TypeConverter
    public static String intArrayToString(Integer[] intArray) {
        if (intArray == null) {
            return null;
        }
        return new Gson().toJson(intArray);
    }

    @TypeConverter
    public static Integer[] stringToIntArray(String intArrayJson) {
        if (intArrayJson == null) {
            return new Integer[0];
        }
        return new Gson().fromJson(intArrayJson, Integer[].class);
    }
}
