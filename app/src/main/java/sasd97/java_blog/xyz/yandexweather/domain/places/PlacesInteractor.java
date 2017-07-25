package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetails;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Places;

/**
 * Created by alexander on 12/07/2017.
 */

public interface PlacesInteractor {
    Observable<Places> getPlaces(@NonNull String s);
    Observable<PlaceDetails> getPlaceDetails(@NonNull String s);
}
