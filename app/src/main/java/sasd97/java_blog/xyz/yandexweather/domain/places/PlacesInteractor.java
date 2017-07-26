package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public interface PlacesInteractor {
    Observable<PlacesResponse> getPlaces(@NonNull String s);
    Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String s);
}
