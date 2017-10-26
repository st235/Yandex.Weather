package suhockii.dev.weather.data.models.places;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

class AddressComponent {
    @SerializedName("long_name")
    private String longName;

    @SerializedName("short_name")
    private String shortName;

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }
}
