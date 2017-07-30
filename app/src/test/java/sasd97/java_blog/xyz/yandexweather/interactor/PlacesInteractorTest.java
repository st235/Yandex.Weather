package sasd97.java_blog.xyz.yandexweather.interactor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlaceDetailsResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractorImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(repo.getPlaceDetails(placeId)).thenReturn(Observable.just(response));

        placesInteractor.getPlaceDetails(placeId).test()
                .assertValue(response);
        verify(repo, times(1)).getPlaceDetails(placeId);
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
}
