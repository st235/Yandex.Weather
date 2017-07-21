package sasd97.java_blog.xyz.yandexweather.background;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import sasd97.java_blog.xyz.yandexweather.data.AppRepository;

/**
 * Created by alexander on 13/07/2017.
 */

public class WeatherJobCreator implements JobCreator {

    private AppRepository repository;

    public WeatherJobCreator(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case UpdateWeatherJob.TAG:
                return new UpdateWeatherJob(repository);
            default:
                return null;
        }
    }
}
