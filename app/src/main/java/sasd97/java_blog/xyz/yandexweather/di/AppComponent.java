package sasd97.java_blog.xyz.yandexweather.di;

import javax.inject.Singleton;

import dagger.Component;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;
import sasd97.java_blog.xyz.yandexweather.presentation.SplashScreenActivity;
import sasd97.java_blog.xyz.yandexweather.presentation.MainActivity;

/**
 * Created by alexander on 09/07/2017.
 */

@Component(modules = {AppModule.class, NavigationModule.class})
@Singleton
public interface AppComponent {
    void inject(SplashScreenActivity activity);
    void inject(MainActivity activity);
}
