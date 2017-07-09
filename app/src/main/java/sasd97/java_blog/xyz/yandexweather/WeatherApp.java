package sasd97.java_blog.xyz.yandexweather;

import android.app.Application;

import sasd97.java_blog.xyz.yandexweather.di.AppComponent;
import sasd97.java_blog.xyz.yandexweather.di.DaggerAppComponent;
import sasd97.java_blog.xyz.yandexweather.di.MainComponent;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.MainModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;

/**
 * Created by alexander on 07/07/2017.
 */

public class WeatherApp extends Application {

    private static AppComponent appComponent;
    private static MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = buildAppComponent();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static MainComponent getMainComponent() {
        if (mainComponent == null) mainComponent = appComponent.plusMainViewSubComponent(new MainModule());
        return mainComponent;
    }

    public AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .navigationModule(new NavigationModule())
                .build();
    }
}
