package sasd97.java_blog.xyz.yandexweather.data.models.places;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim Sukhotski on 7/24/2017.
 */

public class Predictions {
    @SerializedName("place_id")
    String placeId;
    private String id;
    private String description;
    private String reference;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }
}
