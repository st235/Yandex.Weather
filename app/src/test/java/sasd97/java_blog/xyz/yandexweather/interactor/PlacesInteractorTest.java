package sasd97.java_blog.xyz.yandexweather.interactor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.places.LatLng;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractorImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.only;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

@RunWith(JUnit4.class)
public class PlacesInteractorTest {
    private AppRepository repo;
    private PlacesInteractor placesInteractor;

    @Before
    public void setup() {
        repo = mock(AppRepository.class);
        placesInteractor = new PlacesInteractorImpl(repo);
    }

    @Test
    public void getPlaceDetails() {
        String placeId = "placeId";
        PlaceDetailsResponse response = new PlaceDetailsResponse();

        when(repo.getPlaceDetailsById(placeId)).thenReturn(Observable.just(response));

        placesInteractor.getPlaceDetailsById(placeId).test()
                .assertValue(response);
        verify(repo, times(1)).getPlaceDetailsById(placeId);
    }

    @Test
    public void getCachedWeatherWhenExists() {
        String query = "query";
        PlacesResponse response = new PlacesResponse();

        when(repo.getPlaces(query)).thenReturn(Observable.just(response));

        placesInteractor.getPlaces(query).test()
                .assertValue(response);
        verify(repo, times(1)).getPlaces(query);
    }

    @Test
    public void getPlace() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");

        when(repo.getSavedLocationPlace()).thenReturn(Single.just(place));

        placesInteractor.getSavedLocationPlace();
        verify(repo, only()).getSavedLocationPlace();
    }

    @Test
    public void savePlaceToFavorites() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");

        placesInteractor.savePlaceToFavorites(place);
        verify(repo, only()).insertPlace(place);
    }

    @Test
    public void removePlacesFromFavorites() {
        LatLng coords = new LatLng(55.755826, 37.6173);
        String placeName = "Moscow";
        Place place = new Place(placeName, coords, "");
        List<Place> list = new ArrayList<>();
        list.add(place);
        placesInteractor.removePlacesFromFavorites(list);
        verify(repo, only()).removePlaces(list);
    }

    @Test
    public void getPlaces() {
        String placeId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";

        placesInteractor.getPlaces(placeId);
        verify(repo, only()).getPlaces(placeId);
    }
}
