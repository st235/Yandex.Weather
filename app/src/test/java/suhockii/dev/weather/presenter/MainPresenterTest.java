package suhockii.dev.weather.presenter;

import android.support.v4.app.FragmentActivity;

import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.Instant;

import io.reactivex.Observable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import suhockii.dev.weather.R;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.places.PlacesResponse;
import suhockii.dev.weather.data.models.places.Predictions;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.domain.settings.SettingsInteractor;
import suhockii.dev.weather.navigation.AppFragmentRouter;
import suhockii.dev.weather.presentation.main.MainPresenter;
import suhockii.dev.weather.presentation.main.MainView;
import suhockii.dev.weather.utils.RxSchedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Maksim Sukhotski on 7/28/2017.
 */

public class MainPresenterTest {
    private MainView view;
    private MainPresenter presenter;
    private PlacesInteractor placesInteractor;
    private RxSchedulers rxSchedulers;
    private int settingsFragmentId;
    private SettingsInteractor settingsInteractor;
    private String placeId;

    @Before
    public void setup() {
        view = mock(MainView.class);
        FragmentActivity fragmentActivity = mock(FragmentActivity.class);
        rxSchedulers = mock(RxSchedulers.class);
        placesInteractor = mock(PlacesInteractor.class);
        settingsInteractor = mock(SettingsInteractor.class);
        placeId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";
        when(rxSchedulers.getIoToMainTransformerObservable()).thenReturn(objectObservable -> objectObservable);

        presenter = new MainPresenter(rxSchedulers, placesInteractor, settingsInteractor);
        presenter.setWeatherRouter(new AppFragmentRouter(R.id.fragment_container_weather, fragmentActivity));
        presenter.openWeatherFragment();
        settingsFragmentId = R.id.main_activity_navigation_settings;
        presenter.attachView(view);
    }

    @Test
    public void navigateTo() {
        int settingsId = R.id.main_activity_navigation_settings;
        presenter.navigateWeatherTo(settingsId);
        verify(view, only()).closeDrawer();
    }

    @Test
    public void search() {
        String query = "query";
        Predictions[] somePredictions = new Predictions[1];
        somePredictions[0] = new Predictions("placeId", "id", "des", "ref");
        PlacesResponse placesResponse = new PlacesResponse(somePredictions, "OK");

        when(placesInteractor.getPlaces(query)).thenReturn(Observable.just(placesResponse));

        presenter.search(query).test()
                .awaitTerminalEvent();
        verify(view, only()).showSuggestions(placesResponse.getPredictionStrings());
    }

    @Test
    public void onBackClickedWith2ElementStack() {
        presenter.navigateWeatherTo(settingsFragmentId);
        presenter.onBackClicked();
        verify(view, times(1)).closeDrawer();
    }

    @Test
    public void replaceFragment() {
        presenter.replaceWeatherFragment(settingsFragmentId);
        verify(view, times(1)).closeDrawer();
    }

    @Test
    public void navigateWeatherTo() {
        presenter.navigateWeatherTo(settingsFragmentId);
        verify(view, times(1)).closeDrawer();
    }

    @Test
    public void saveCurrentPlace() {
        Place place = new Place(placeId, "Москва",
                new LatLng(0.0, 0.0), (int) Instant.now().getEpochSecond());
        Place toReplace = new Place("ChIJybDUc_xKtUYRTM9XV8zWRD0", "Москва",
                new LatLng(0.0, 0.0), (int) Instant.now().getEpochSecond());

        when(rxSchedulers.getIoToMainTransformerCompletable()).thenReturn(t -> t);
        when(placesInteractor.savePlaceToFavorites(toReplace)).thenReturn(new CompletableFromAction(() -> {
        }));
        when(placesInteractor.savePlaceToFavorites(place)).thenReturn(new CompletableFromAction(() -> {
        }));

        presenter.saveCurrentPlace(place, toReplace);
        verify(placesInteractor, times(1)).savePlaceToFavorites(place);
    }
}