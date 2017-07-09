package sasd97.java_blog.xyz.yandexweather;

import android.app.Application;

import sasd97.java_blog.xyz.yandexweather.di.AppComponent;
import sasd97.java_blog.xyz.yandexweather.di.DaggerAppComponent;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;

/**
 * Created by alexander on 07/07/2017.
 */

public class WeatherApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = buildAppComponent();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .navigationModule(new NavigationModule())
                .build();
    }
}
