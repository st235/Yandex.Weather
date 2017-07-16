package sasd97.java_blog.xyz.yandexweather.di;

import javax.inject.Singleton;

import dagger.Component;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.DataModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.JobModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.MainModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;

/**
 * Created by alexander on 09/07/2017.
 */

@Component(modules = {AppModule.class, DataModule.class, JobModule.class, NavigationModule.class})
@Singleton
public interface AppComponent {
    void inject(WeatherApp app);

    MainComponent plusMainComponent(MainModule module);
}
