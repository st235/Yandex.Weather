package suhockii.dev.weather.background;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.google.gson.Gson;

import suhockii.dev.weather.data.AppRepository;

/**
 * Created by alexander on 13/07/2017.
 */

public class WeatherJobCreator implements JobCreator {

    private Gson gson;
    private AppRepository repository;

    public WeatherJobCreator(@NonNull Gson gson,
                             @NonNull AppRepository repository) {
        this.gson = gson;
        this.repository = repository;
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case UpdateWeatherJob.TAG:
                return new UpdateWeatherJob(gson, repository);
            default:
                return null;
        }
    }
}
