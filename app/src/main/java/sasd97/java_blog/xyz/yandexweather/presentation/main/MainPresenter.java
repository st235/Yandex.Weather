package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Stack;

import javax.inject.Inject;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.AddToBackStack;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.FragmentCommand;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.Replace;
import sasd97.java_blog.xyz.yandexweather.presentation.about.AboutFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.settings.SettingsFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

/**
 * Created by alexander on 09/07/2017.
 */

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private final PlacesInteractor interactor;
    private final RxSchedulers schedulers;
    private Router<FragmentCommand> fragmentRouter;
    private Stack<Integer> menuItemsStack = new Stack<>();

    @Inject
    public MainPresenter(@NonNull RxSchedulers schedulers,
                         @NonNull PlacesInteractor interactor) {
        this.schedulers = schedulers;
        this.interactor = interactor;
    }

    public void setRouter(Router<FragmentCommand> fragmentRouter) {
        this.fragmentRouter = fragmentRouter;
    }

    public void openWeatherFragment() {
        fragmentRouter.pushForward(new Replace(WeatherFragment.newInstance()));
        menuItemsStack.push(R.id.main_activity_navigation_weather);
    }

    public void onBackClicked() {
        menuItemsStack.pop();
        if (menuItemsStack.isEmpty()) return;
        int id = menuItemsStack.peek();
        getViewState().selectNavigationItem(id);
    }

    public void navigateTo(@IdRes int id) {
        if (isSameFragmentAtTheTop(id)) {
            getViewState().closeDrawer();
            return;
        }

        replaceFragment(id);
    }

    private boolean isSameFragmentAtTheTop(@IdRes int id) {
        return id == menuItemsStack.peek();
    }

    private void replaceFragment(@IdRes int id) {
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
        fragmentRouter.pushForward(replace);
        getViewState().closeDrawer();
    }

    public void search(String query) {
        interactor.getPlaces(query)
                .compose(schedulers.getIoToMainTransformer())
                .subscribe(places -> {

                }, throwable -> {

                });
    }
}
