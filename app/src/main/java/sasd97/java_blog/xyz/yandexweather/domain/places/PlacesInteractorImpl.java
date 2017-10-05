package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public class PlacesInteractorImpl implements PlacesInteractor {

    private static final String GPS_IS_OFF = "Gps is off";
    private static final String EMPTY_NAME = "";
    private AppRepository repository;

    public PlacesInteractorImpl(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<PlacesResponse> getPlaces(@NonNull String s) {
        return repository.getPlaces(s);
    }

    @Override
    public Observable<PlaceDetailsResponse> getPlaceDetails(@NonNull String placeId) {
        return repository.getPlaceDetails(placeId);
    }

    @NonNull
    @Override
    public Single<Place> getSavedLocationPlace() {
        return repository.getSavedLocationPlace();
    }

    @Override
    public Single<Place> getCurrentLocation() {
        return Single.fromCallable(() -> {
            // noinspection MissingPermission
            Location location = repository.getCurrentLocation();
            if (location == null) {
                throw new Resources.NotFoundException(GPS_IS_OFF);
            }
            return new Place(EMPTY_NAME, new Pair<>(location.getLatitude(), location.getLongitude()));
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
    public Completable savePlace(@NonNull Place place) {
        return repository.savePlace(place);
    }
}
