package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.places.LatLng;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public class PlacesInteractorImpl implements PlacesInteractor {

    private static final String GPS_IS_OFF = "Gps is off";
    private AppRepository repository;

    public PlacesInteractorImpl(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<PlacesResponse> getPlaces(@NonNull String s) {
        return repository.getPlaces(s);
    }

    @Override
    public Observable<PlaceDetailsResponse> getPlaceDetailsById(@NonNull String placeId) {
        return repository.getPlaceDetailsById(placeId);
    }

    @Override
    public Single<PlaceDetailsResponse> getPlaceDetailsByCoords(@NonNull LatLng coords) {
        return repository.getPlaceDetailsByCoords(coords, Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public Single<Place> getSavedLocationPlace() {
        return repository.getSavedLocationPlace();
    }

    @Override
    @SuppressWarnings({"ResourceType"})
    public Single<LatLng> getCurrentCoords() {
        return Single.fromCallable(() -> {
            Location location = repository.getCurrentLocation();
            if (location == null) {
                throw new Resources.NotFoundException(GPS_IS_OFF);
            }
            return new LatLng(location.getLatitude(), location.getLongitude());
        });
    }

    @Override
    public Completable savePlaceToFavorites(Place place) {
        return repository.insertPlace(place);
    }

    @Override
    public Single<List<Place>> getFavoritePlaces() {
        return repository.getFavoritePlaces();
    }

    @Override
    public Completable removePlacesFromFavorites(List<Place> places) {
        return repository.removePlaces(places);
    }

    @Override
    public void savePlace(@NonNull Place place) {
        repository.savePlace(place);
    }
}
