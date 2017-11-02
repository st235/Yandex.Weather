package suhockii.dev.weather.presentation.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Date;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import suhockii.dev.weather.R;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.places.PlacesResponse;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.domain.settings.SettingsInteractor;
import suhockii.dev.weather.navigation.Router;
import suhockii.dev.weather.navigation.fragments.AddToBackStack;
import suhockii.dev.weather.navigation.fragments.FragmentCommand;
import suhockii.dev.weather.navigation.fragments.Replace;
import suhockii.dev.weather.presentation.about.AboutFragment;
import suhockii.dev.weather.presentation.drawer.DrawerFragment;
import suhockii.dev.weather.presentation.settings.SettingsFragment;
import suhockii.dev.weather.presentation.weather.WeatherFragment;
import suhockii.dev.weather.utils.RxSchedulers;

import static suhockii.dev.weather.presentation.drawer.DrawerFragment.TAG_NAVIGATION;
import static suhockii.dev.weather.presentation.weather.WeatherFragment.TAG_WEATHER;

/**
 * Created by alexander on 09/07/2017.
 */

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private final PlacesInteractor placesInteractor;
    private final SettingsInteractor settingsInteractor;
    private final RxSchedulers schedulers;
    private Stack<Integer> menuItemsStack = new Stack<>();
    private PlacesResponse placesResponse;
    private Router<FragmentCommand> weatherFragmentRouter;
    private Router<FragmentCommand> navigationFragmentRouter;

    @Inject
    public MainPresenter(@NonNull RxSchedulers schedulers,
                         @NonNull PlacesInteractor placesInteractor,
                         @NonNull SettingsInteractor settingsInteractor) {
        this.schedulers = schedulers;
        this.placesInteractor = placesInteractor;
        this.settingsInteractor = settingsInteractor;
    }

    public void setWeatherRouter(Router<FragmentCommand> weatherFragmentRouter) {
        this.weatherFragmentRouter = weatherFragmentRouter;
    }

    public void setNavigationRouter(Router<FragmentCommand> navigationFragmentRouter) {
        this.navigationFragmentRouter = navigationFragmentRouter;
    }

    public void openWeatherFragment() {
        weatherFragmentRouter.pushForward(new Replace(WeatherFragment.newInstance()), TAG_WEATHER);
    }

    public void openNavigationFragment() {
        navigationFragmentRouter.pushForward(new Replace(DrawerFragment.newInstance()), TAG_NAVIGATION);
    }

    public void onBackClicked() {
        if (menuItemsStack.isEmpty()) {
            getViewState().closeActivity();
            return;
        }
        menuItemsStack.pop();
    }

    public void navigateWeatherTo(@IdRes int id) {
        if (isSameFragmentAtTheTop(id)) {
            getViewState().closeDrawer();
            return;
        }

        replaceWeatherFragment(id);
    }

    private boolean isSameFragmentAtTheTop(@IdRes int id) {
        return !menuItemsStack.isEmpty() && id == menuItemsStack.peek();
    }

    public void replaceWeatherFragment(@IdRes int id) {
        Replace replace;
        menuItemsStack.add(id);

        switch (id) {
            case R.id.main_activity_navigation_weather:
                replace = new Replace(WeatherFragment.newInstance());
                break;
            case R.id.main_activity_navigation_about:
                replace = new Replace(AboutFragment.newInstance());
                break;
            case R.id.main_activity_navigation_settings:
                replace = new Replace(SettingsFragment.newInstance());
                break;
            default:
                return;
        }

        replace.setNext(new AddToBackStack());
        weatherFragmentRouter.pushForward(replace);
        getViewState().closeDrawer();
    }

    public Completable search(String query) {
        return placesInteractor.getPlaces(query)
                .compose(schedulers.getIoToMainTransformerObservable())
                .filter(PlacesResponse::isSuccess)
                .doOnNext(this::setPlacesResponse)
                .map(PlacesResponse::getPredictionStrings)
                .flatMapCompletable(strings -> Completable.fromAction(() -> getViewState().showSuggestions(strings)));
    }

    private void setPlacesResponse(PlacesResponse placesResponse) {
        this.placesResponse = placesResponse;
    }

    void updateCurrentPlace(int position, boolean addToFavorites) {
        placesInteractor.getPlaceDetailsById(placesResponse.getPlaceIdAt(position))
                .map(placeDetailsResponse -> new Place(
                        placesResponse.getPlaceIdAt(position),
                        placesResponse.getPlaceNameAt(position),
                        placeDetailsResponse.getCoords(),
                        (int) (new Date().getTime() / 1000)))
                .compose(schedulers.getIoToMainTransformerObservable())
                .doOnNext(place -> {
                    if (addToFavorites) {
                        getViewState().showNewFavoritePlace(place);
                    }
                    placesInteractor.updateCurrentPlace(place);
                })
                .doOnComplete(() -> getViewState().updateWeatherContent())
                .filter(place -> addToFavorites)
                .subscribe(place -> placesInteractor.savePlaceToFavorites(place)
                                .compose(schedulers.getIoToMainTransformerCompletable())
                                .subscribe(),
                        Throwable::printStackTrace);
    }


    public void saveCurrentPlace(Place place, Place toReplace) {
        placesInteractor.savePlaceToFavorites(place)
                .andThen(placesInteractor.savePlaceToFavorites(toReplace))
                .doOnComplete(() -> placesInteractor.updateCurrentPlace(place))
                .compose(schedulers.getIoToMainTransformerCompletable())
                .delay(250, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> getViewState().updateWeatherContent())
                .subscribe(() -> {}, Throwable::printStackTrace);
    }
}
