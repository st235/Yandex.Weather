package suhockii.dev.weather.di;

import javax.inject.Singleton;

import dagger.Component;
import suhockii.dev.weather.WeatherApp;
import suhockii.dev.weather.di.modules.AppModule;
import suhockii.dev.weather.di.modules.DataModule;
import suhockii.dev.weather.di.modules.JobModule;
import suhockii.dev.weather.di.modules.MainModule;
import suhockii.dev.weather.di.modules.NavigationModule;

/**
 * Created by alexander on 09/07/2017.
 */

@Component(modules = {AppModule.class, DataModule.class, JobModule.class, NavigationModule.class})
@Singleton
public interface AppComponent {
    void inject(WeatherApp app);

    MainComponent plusMainComponent(MainModule module);
}
