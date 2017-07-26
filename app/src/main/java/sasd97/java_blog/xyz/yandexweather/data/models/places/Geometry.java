package sasd97.java_blog.xyz.yandexweather.data.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/25/2017.
 */

class Geometry {
    @SerializedName("location")
    @Expose
    Location location;
}
