package sasd97.java_blog.xyz.yandexweather;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import sasd97.java_blog.xyz.yandexweather.di.AppComponent;
import sasd97.java_blog.xyz.yandexweather.di.DaggerAppComponent;
import sasd97.java_blog.xyz.yandexweather.di.MainComponent;
import sasd97.java_blog.xyz.yandexweather.di.SplashScreenComponent;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.MainModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.SplashScreenModule;

/**
 * Created by alexander on 07/07/2017.
 */

public class WeatherApp extends Application {

    private AppComponent appComponent;
    private MainComponent mainComponent;
    private SplashScreenComponent splashScreenComponent;

    public static WeatherApp get(@NonNull Context context) {
        return (WeatherApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = buildAppComponent();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public MainComponent getMainComponent() {
        if (mainComponent == null) mainComponent = appComponent.plusMainComponent(new MainModule());
        return mainComponent;
    }

    public SplashScreenComponent getSplashScreenComponent() {
        if (splashScreenComponent == null) splashScreenComponent = appComponent.plusSplashScreenComponent(new SplashScreenModule());
        return splashScreenComponent;
    }

    private AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .navigationModule(new NavigationModule())
                .build();
    }
}
