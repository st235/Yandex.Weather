package sasd97.java_blog.xyz.yandexweather.presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.NavigationPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.NavigationView;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

public class NavigationPresenterTest {
    private NavigationView view;
    private NavigationPresenter presenter;
    private PlacesInteractor interactor;
    private RxSchedulers rxSchedulers;

    @Before
    public void setup() {
        view = mock(NavigationView.class);
        rxSchedulers = mock(RxSchedulers.class);
        interactor = mock(PlacesInteractor.class);
        presenter = new NavigationPresenter(rxSchedulers, interactor);
        when(rxSchedulers.getIoToMainTransformerCompletable()).thenReturn(t -> t);
        when(rxSchedulers.getComputationToMainTransformerSingle()).thenReturn(t -> t);
        when(interactor.getFavoritePlaces()).thenReturn(Single.fromCallable(ArrayList::new));
        presenter.attachView(view);
    }

    @Test
    public void getCurrentInterval() {
        ArrayList<sasd97.java_blog.xyz.yandexweather.data.models.places.Place> places = new ArrayList<>();
        when(interactor.removePlacesFromFavorites(places)).thenReturn(Completable.fromAction(() -> {}));
        presenter.removeSelectedPlaces(places);
        verify(interactor, times(1)).removePlacesFromFavorites(places);
    }
}