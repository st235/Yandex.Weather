package suhockii.dev.weather.data.models.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 8/9/2017.
 */

public class Temperature {
    @SerializedName("day")
    @Expose
    private float day;

    @SerializedName("min")
    @Expose
    private float minimum;

    @SerializedName("max")
    @Expose
    private float maximum;

    @SerializedName("night")
    @Expose
    private float night;

    @SerializedName("eve")
    @Expose
    private float evening;

    @SerializedName("morn")
    @Expose
    private float morning;

    public float getDay() {
        return day;
    }

    public float getMinimum() {
        return minimum;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getNight() {
        return night;
    }

    public float getEvening() {
        return evening;
    }

    public float getMorning() {
        return morning;
    }
}
