package sasd97.java_blog.xyz.yandexweather.data.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

class Location {
    @SerializedName("lng")
    @Expose
    Double lng;

    @SerializedName("lat")
    @Expose
    Double lat;
}
