package suhockii.dev.weather.domain.places;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.places.PlaceDetailsResponse;
import suhockii.dev.weather.data.models.places.PlacesResponse;

/**
 * Created by alexander on 12/07/2017.
 */

public class PlacesInteractorImpl implements PlacesInteractor {

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
    public Single<Place> getCurrentPlace() {
        return repository.getCurrentPlace();
    }

    @Override
    @SuppressWarnings({"ResourceType"})
    public Single<LatLng> getCurrentCoords() {
        return Single.create(emitter -> repository.getCurrentLocation(emitter));
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
    public void updateCurrentPlace(@NonNull Place place) {
        repository.updateCurrentPlace(place);
    }
}
