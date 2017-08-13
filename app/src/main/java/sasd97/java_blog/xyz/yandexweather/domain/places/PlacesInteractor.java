package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public interface PlacesInteractor {
    Observable<PlacesResponse> getPlaces(@NonNull String s);
    Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String placeId);

    @NonNull
    Place getPlace();

    //db
    Single<List<Place>> getFavoritePlaces();
    Completable savePlaceToFavorites(Place place);
    Completable removePlacesFromFavorites(List<Place> places);
}
