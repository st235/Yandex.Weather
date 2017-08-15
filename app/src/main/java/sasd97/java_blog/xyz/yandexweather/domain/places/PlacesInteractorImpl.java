package sasd97.java_blog.xyz.yandexweather.domain.places;

import android.support.annotation.NonNull;

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

    private static final String TAG = PlacesInteractorImpl.class.getCanonicalName();
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
    public Place getPlace() {
        return repository.getPlace();
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
}
