package suhockii.dev.weather;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;
import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import suhockii.dev.weather.background.UpdateWeatherJob;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.di.AppComponent;
import suhockii.dev.weather.di.DaggerAppComponent;
import suhockii.dev.weather.di.MainComponent;
import suhockii.dev.weather.di.modules.AppModule;
import suhockii.dev.weather.di.modules.MainModule;
import suhockii.dev.weather.di.modules.NavigationModule;
import suhockii.dev.xyz.richtextview.FontProvider;
import timber.log.Timber;

/**
 * Created by alexander on 07/07/2017.
 */

public class WeatherApp extends Application {
    public static final String SPACE = " ";

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private AppComponent appComponent;
    private MainComponent mainComponent;

    @Inject JobManager jobManager;
    @Inject AppRepository repository;

    public static WeatherApp get(@NonNull Context context) {
        return (WeatherApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = buildAppComponent();
        getAppComponent().inject(this);
        onInit();
    }

    private void onInit() {
        FontProvider.init(getAssets());
        AndroidThreeTen.init(this);
        onScheduleJob();
        Timber.plant(new Timber.DebugTree());
    }

    private void onScheduleJob() {
        if (!repository.isBackgroundServiceEnabled()) return;
        if (jobManager.getAllJobRequestsForTag(UpdateWeatherJob.TAG).size() > 0) return;
        UpdateWeatherJob.scheduleJob(repository.getWeatherUpdateInterval());
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
