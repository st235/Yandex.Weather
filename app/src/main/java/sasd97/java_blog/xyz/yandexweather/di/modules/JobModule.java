package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.background.WeatherJobCreator;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;

/**
 * Created by alexander on 13/07/2017.
 */

@Module
public class JobModule {

    @Provides
    @Singleton
    @NonNull
    public JobManager provideJobManager(Context context, WeatherJobCreator creator) {
        JobManager manager = JobManager.create(context);
        manager.addJobCreator(creator);
        return manager;
    }

    @Provides
    @Singleton
    @NonNull
    public WeatherJobCreator provideWeatherJobCreator(Gson gson, AppRepository repository) {
        return new WeatherJobCreator(gson, repository);
    }
}

