package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.FragmentCommand;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.PushToBackStack;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.Replace;
import sasd97.java_blog.xyz.yandexweather.presentation.about.AboutFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.settings.SettingsFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment;

/**
 * Created by alexander on 09/07/2017.
 */

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private Router<FragmentCommand> fragmentRouter;

    public void setRouter(Router<FragmentCommand> fragmentRouter) {
        this.fragmentRouter = fragmentRouter;
    }

    public void open() {
        fragmentRouter.pushForward(new Replace(WeatherFragment.newInstance()));
    }

    public void navigateTo(@IdRes int id) {
        Replace replace;

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

        replace.setNext(new PushToBackStack());
        fragmentRouter.pushForward(replace);
        getViewState().closeDrawer();
    }
}
