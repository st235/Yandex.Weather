package sasd97.java_blog.xyz.yandexweather.presentation.splash;

import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.ActivityCommand;

/**
 * Created by alexander on 09/07/2017.
 */

public class SplashScreenPresenter extends MvpPresenter<SplashScreenView> {

    private Router<ActivityCommand> router;

    public SplashScreenPresenter(Router<ActivityCommand> router) {
        this.router = router;
    }

    public void onLoad(ActivityCommand command) {
        router.pushForward(command);
    }
}
