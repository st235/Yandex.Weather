package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by alexander on 12/07/2017.
 */

public interface PlacesInteractor {
    Observable<PlacesResponse> getPlaces(@NonNull String s);
    Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String placeId);

    @NonNull
    Single<Place> getUserLocationPlace();

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    Single<Place> getCurrentLocation();
    //db
    Single<List<Place>> getFavoritePlaces();
    Completable savePlaceToFavorites(Place place);
    Completable removePlacesFromFavorites(List<Place> places);

    Completable savePlace(@NonNull Place place);
}
