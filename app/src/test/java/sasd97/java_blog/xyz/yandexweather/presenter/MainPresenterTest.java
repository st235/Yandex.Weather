package sasd97.java_blog.xyz.yandexweather.presenter;

import android.support.v4.app.FragmentActivity;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Predictions;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.navigation.AppFragmentRouter;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainView;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

    @Before
    public void setup() {
        view = mock(MainView.class);
        FragmentActivity fragmentActivity = mock(FragmentActivity.class);
        rxSchedulers = mock(RxSchedulers.class);
        placesInteractor = mock(PlacesInteractor.class);
        SettingsInteractor settingsInteractor = mock(SettingsInteractor.class);

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
        presenter.weatherNavigateTo(settingsId);
        verify(view, only()).closeDrawer();
    }

    @Test
    public void search() {
        String query = "query";
        Predictions[] somePredictions = new Predictions[1];
        somePredictions[0] = new Predictions("placeId", "id", "des", "ref");
        PlacesResponse placesResponse = new PlacesResponse(somePredictions, "OK");

        when(placesInteractor.getPlaces(query)).thenReturn(Observable.just(placesResponse));
        when(rxSchedulers.getIoToMainTransformer()).thenReturn(objectObservable -> objectObservable);

        presenter.search(query).test()
                .awaitTerminalEvent();
        verify(view, only()).showSuggestions(placesResponse.getPredictionStrings());
    }

    @Test
    public void onBackClickedWith1ElementInStack() {
        presenter.onBackClicked();
        verify(view, never()).selectNavigationItem(weatherFragmentId);
    }

    @Test
    public void onBackClickedWith2ElementStack() {
        presenter.weatherNavigateTo(settingsFragmentId);
        presenter.onBackClicked();
        verify(view, times(1)).closeDrawer();
    }

    @Test
    public void replaceFragment() {
        presenter.replaceWeatherFragment(settingsFragmentId);
        verify(view, times(1)).closeDrawer();
    }
}