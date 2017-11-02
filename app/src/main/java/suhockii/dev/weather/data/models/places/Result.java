package suhockii.dev.weather.data.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

class Result {
    @SerializedName("geometry")
    @Expose
    Geometry geometry;

    @SerializedName("address_components")
    @Expose
    AddressComponent[] addressComponents;

    @SerializedName("formatted_address")
    @Expose
    String formattedAddress;

    @SerializedName("place_id")
    String placeId;
}
