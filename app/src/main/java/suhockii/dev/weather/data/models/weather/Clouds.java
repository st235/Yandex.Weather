package suhockii.dev.weather.data.models.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    @Expose
    private int percentile;

    public int getPercentile() {
        return percentile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Clouds{");
        sb.append("percentile=").append(percentile);
        sb.append('}');
        return sb.toString();
    }
}
