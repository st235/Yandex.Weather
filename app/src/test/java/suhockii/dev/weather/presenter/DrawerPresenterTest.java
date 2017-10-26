package suhockii.dev.weather.presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.presentation.drawer.DrawerPresenter;
import suhockii.dev.weather.presentation.drawer.DrawerView;
import suhockii.dev.weather.utils.RxSchedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

public class DrawerPresenterTest {
    private DrawerView view;
    private DrawerPresenter presenter;
    private PlacesInteractor interactor;
    private RxSchedulers rxSchedulers;

    @Before
    public void setup() {
        view = mock(DrawerView.class);
        rxSchedulers = mock(RxSchedulers.class);
        interactor = mock(PlacesInteractor.class);
        presenter = new DrawerPresenter(rxSchedulers, interactor);
        when(rxSchedulers.getIoToMainTransformerCompletable()).thenReturn(t -> t);
        when(rxSchedulers.getComputationToMainTransformerSingle()).thenReturn(t -> t);
        when(interactor.getFavoritePlaces()).thenReturn(Single.fromCallable(ArrayList::new));
        presenter.attachView(view);
    }

    @Test
    public void removeSelectedPlaces() {
        ArrayList<suhockii.dev.weather.data.models.places.Place> places = new ArrayList<>();
        when(interactor.removePlacesFromFavorites(places)).thenReturn(Completable.fromAction(() -> {}));
        presenter.removeSelectedPlaces(places);
        verify(interactor, times(1)).removePlacesFromFavorites(places);
    }
}