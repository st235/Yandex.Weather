package sasd97.java_blog.xyz.yandexweather;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.evernote.android.job.JobManager;

import javax.inject.Inject;

import sasd97.java_blog.xyz.yandexweather.background.UpdateWeatherJob;
import sasd97.java_blog.xyz.yandexweather.di.AppComponent;
import sasd97.java_blog.xyz.yandexweather.di.DaggerAppComponent;
import sasd97.java_blog.xyz.yandexweather.di.MainComponent;
import sasd97.java_blog.xyz.yandexweather.di.modules.AppModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.MainModule;
import sasd97.java_blog.xyz.yandexweather.di.modules.NavigationModule;
import sasd97.java_blog.xyz.yandexweather.utils.FontUtils;

/**
 * Created by alexander on 07/07/2017.
 */

public class WeatherApp extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private AppComponent appComponent;
    private MainComponent mainComponent;

    @Inject FontUtils fontUtils;
    @Inject JobManager jobManager;

    public static WeatherApp get(@NonNull Context context) {
        return (WeatherApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = buildAppComponent();
        getAppComponent().inject(this);
        onInit();
    }

    private void onInit() {
        UpdateWeatherJob.scheduleJob();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public MainComponent getMainComponent() {
        if (mainComponent == null) mainComponent
                = appComponent.plusMainComponent(new MainModule());
        return mainComponent;
    }

    private AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .navigationModule(new NavigationModule())
                .build();
    }
}
