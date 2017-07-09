package sasd97.java_blog.xyz.yandexweather.di.modules;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.di.scopes.SplashScreenScope;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.ActivityCommand;
import sasd97.java_blog.xyz.yandexweather.presentation.splash.SplashScreenPresenter;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
@SplashScreenScope
public class SplashScreenModule {

    @Provides
    @SplashScreenScope
    public SplashScreenPresenter provideSplashScreenPresenter(Router<ActivityCommand> router) {
        return new SplashScreenPresenter(router);
    }
}
