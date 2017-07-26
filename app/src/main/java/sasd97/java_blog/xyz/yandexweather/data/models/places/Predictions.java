package sasd97.java_blog.xyz.yandexweather.data.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/24/2017.
 */

class Predictions {
    @SerializedName("place_id")
    @Expose
    String placeId;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("reference")
    @Expose
    private String reference;

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }
}