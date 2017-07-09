package sasd97.java_blog.xyz.yandexweather.di;

import javax.inject.Singleton;

import dagger.Component;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.MainModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.SplashScreenModule;
import sasd97.java_blog.xyz.yandexweather.presentation.splash.SplashScreenActivity;

/**
 * Created by alexander on 09/07/2017.
 */

@Component(modules = {AppModule.class, NavigationModule.class})
@Singleton
public interface AppComponent {
    MainComponent plusMainComponent(MainModule module);
    SplashScreenComponent plusSplashScreenComponent(SplashScreenModule module);
}
