package sasd97.java_blog.xyz.yandexweather.di;

import dagger.Subcomponent;
import sasd97.java_blog.xyz.yandexweather.di.modules.SplashScreenModule;
import sasd97.java_blog.xyz.yandexweather.di.scopes.SplashScreenScope;
import sasd97.java_blog.xyz.yandexweather.presentation.splash.SplashScreenActivity;
import sasd97.java_blog.xyz.yandexweather.presentation.splash.SplashScreenPresenter;

/**
 * Created by alexander on 09/07/2017.
 */

@Subcomponent(modules = {SplashScreenModule.class})
@SplashScreenScope
public interface SplashScreenComponent {
    void inject(SplashScreenActivity activity);

    SplashScreenPresenter getSplashScreenPresenter();
}
