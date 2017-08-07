package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Stack;

import javax.inject.Inject;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.places.PlacesResponse;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.AddToBackStack;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.FragmentCommand;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.Replace;
import sasd97.java_blog.xyz.yandexweather.presentation.about.AboutFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.NavigationFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.settings.SettingsFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

/**
 * Created by alexander on 09/07/2017.
 */

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public static final String TAG = "navigation";
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
        weatherFragmentRouter.pushForward(new Replace(WeatherFragment.newInstance()));
        menuItemsStack.push(R.id.main_activity_navigation_weather);
    }

    public void openNavigationFragment() {
        navigationFragmentRouter.pushForward(new Replace(NavigationFragment.newInstance()), TAG);
    }

    public void onBackClicked() {
        menuItemsStack.pop();
        if (menuItemsStack.isEmpty()) return;
    }

    /**
     * Sorry, but Alexander`s FragmentMananager wrapper is very complicated.
     * So navigation will not be plain.
     */
    public void navigateWeatherTo(@IdRes int id) {
        if (isSameFragmentAtTheTop(id)) {
            getViewState().closeDrawer();
            return;
        }

        replaceWeatherFragment(id);
    }

    private boolean isSameFragmentAtTheTop(@IdRes int id) {
        return menuItemsStack.size() > 0 && id == menuItemsStack.peek();
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

    public Observable<String[]> search(String query) {
        return placesInteractor.getPlaces(query)
                .compose(schedulers.getIoToMainTransformer())
                .filter(PlacesResponse::isSuccess)
                .doOnNext(this::setPlacesResponse)
                .map(PlacesResponse::getPredictionStrings)
                .doOnNext(getViewState()::showSuggestions);
    }

    private void setPlacesResponse(PlacesResponse placesResponse) {
        this.placesResponse = placesResponse;
    }

    void saveCity(int position) {
        placesInteractor.getPlaceDetails(placesResponse.getPlaceIdAt(position))
                .compose(schedulers.getIoToMainTransformer())
                .flatMapCompletable(placeDetails -> settingsInteractor.savePlace(
                        new Place(placesResponse.getPlaceNameAt(position),
                                placeDetails.getCoords())))
                .doOnComplete(this::openWeatherFragment)
                .subscribe();
    }
}
