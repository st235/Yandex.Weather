package sasd97.java_blog.xyz.yandexweather.data.models.places;

/**
 * Created by Maksim Sukhotski on 7/24/2017.
 */

public class PlacesResponse {
    private static final String STATUS_OK = "OK";
    private Predictions[] predictions;
    private String status;

    public String getPlaceIdAt(int i) {
        return predictions[i].placeId;
    }

    public String getPlaceNameAt(int i) {
        return predictions[i].description;
    }

    public boolean isSuccess() {
        return status.equals(STATUS_OK);
    }

    public String[] getPredictionStrings() {
        String[] strings = new String[predictions.length];
        for (int i = 0; i < predictions.length; i++)
            strings[i] = predictions[i].description;
        return strings;
    }
}
