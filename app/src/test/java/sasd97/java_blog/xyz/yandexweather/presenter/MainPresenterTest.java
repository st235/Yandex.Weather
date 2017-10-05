package sasd97.java_blog.xyz.yandexweather.presenter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.Instant;

import io.reactivex.Observable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Predictions;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.navigation.AppFragmentRouter;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainView;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

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
    private int weatherFragmentId;
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
        weatherFragmentId = R.id.main_activity_navigation_weather;
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
                new Pair<>(0.0, 0.0), (int) Instant.now().getEpochSecond());
        Place toReplace = new Place("ChIJybDUc_xKtUYRTM9XV8zWRD0", "Москва",
                new Pair<>(0.0, 0.0), (int) Instant.now().getEpochSecond());

        when(rxSchedulers.getIoToMainTransformerCompletable()).thenReturn(t -> t);
        when(placesInteractor.savePlaceToFavorites(toReplace)).thenReturn(new CompletableFromAction(() -> {
        }));
        when(placesInteractor.savePlaceToFavorites(place)).thenReturn(new CompletableFromAction(() -> {
        }));
        when(settingsInteractor.savePlace(place)).thenReturn(new CompletableFromAction(() -> {
        }));

        presenter.saveCurrentPlace(place, toReplace);
        verify(placesInteractor, times(1)).savePlaceToFavorites(place);
    }
}