package suhockii.dev.weather.data.net;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import suhockii.dev.weather.data.models.places.PlaceDetailsResponse;
import suhockii.dev.weather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public interface PlacesApi {
    String BASE_URL = "https://maps.googleapis.com/maps/api/";

    @GET("place/autocomplete/json")
    Observable<PlacesResponse> getPlaces(@Query("input") String input,
                                         @Query("key") String apiKey);

    @GET("place/details/json")
    Observable<PlaceDetailsResponse> getPlaceDetailsById(@Query("placeid") String placeId,
                                                         @Query("key") String apiKey);

    @GET("geocode/json")
    Single<PlaceDetailsResponse> getPlaceDetailsByCoords(@Query("latlng") String latlng,
                                                         @Query("language") String lang,
                                                         @Query("key") String apiKey);
}
