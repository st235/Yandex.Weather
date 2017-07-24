package sasd97.java_blog.xyz.yandexweather.data.models.places;

/**
 * Created by Maksim Sukhotski on 7/24/2017.
 */

public class Places {
    private static final String STATUS_OK = "OK";
    private Predictions[] predictions;
    private String status;

    public Predictions[] getPredictions() {
        return predictions;
    }

    public boolean isSuccess() {
        return status.equals(STATUS_OK);
    }
}
