package sasd97.java_blog.xyz.yandexweather.data.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public interface PlacesApi {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    @GET("autocomplete/json")
    Observable<PlacesResponse> getPlaces(@Query("input") String input, @Query("key") String apiKey);

    @GET("details/json")
    Observable<PlaceDetailsResponse> getPlaceDetails(@Query("placeid") String input, @Query("key") String apiKey);
}
