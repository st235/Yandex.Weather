package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.presentation.about.AboutFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.settings.SettingsFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment;

/**
 * Created by alexander on 09/07/2017.
 */

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private Router<Fragment> fragmentRouter;

    public void setRouter(Router<Fragment> fragmentRouter) {
        this.fragmentRouter = fragmentRouter;
    }

    public void open() {
        fragmentRouter.pushForward(WeatherFragment.newInstance());
    }

    public void navigateTo(@IdRes int id) {
        switch (id) {
            case R.id.main_activity_navigation_weather:
                fragmentRouter.pushForward(WeatherFragment.newInstance());
                break;
            case R.id.main_activity_navigation_about:
                fragmentRouter.pushForward(AboutFragment.newInstance());
                break;
            case R.id.main_activity_navigation_settings:
                fragmentRouter.pushForward(SettingsFragment.newInstance());
                break;
        }

        getViewState().closeDrawer();
    }
}
