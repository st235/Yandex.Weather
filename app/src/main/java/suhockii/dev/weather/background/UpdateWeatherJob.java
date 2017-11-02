package suhockii.dev.weather.background;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import suhockii.dev.weather.data.AppRepository;

/**
 * Created by alexander on 13/07/2017.
 */

public class UpdateWeatherJob extends Job {

    public static final String TAG = "job.weather.update";

    private Gson gson;
    private AppRepository repository;

    public UpdateWeatherJob(@NonNull Gson gson,
                            @NonNull AppRepository repository) {
        this.gson = gson;
        this.repository = repository;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Log.i(TAG, "Update had been started");

        repository.getCurrentPlace()
                .flatMap(repository::getWeather)
                .map(weatherModel -> gson.toJson(weatherModel))
                .zipWith(repository.getCurrentPlace(), Pair::new)
                .map(pair -> repository.saveWeatherToCache(pair.second, pair.first))
                .subscribe(completable -> {}, Throwable::printStackTrace);

        return Result.SUCCESS;
    }

    public static int scheduleJob(int minutes) {
        return new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(minutes), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }
}
