package sasd97.java_blog.xyz.yandexweather.background;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import sasd97.java_blog.xyz.yandexweather.data.AppRepository;

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

        repository
                .getWeather(repository.getPlace())
                .doOnError(Throwable::printStackTrace)
                .subscribe(weather -> {
                    Log.i(TAG, weather.toString());
                    repository.saveWeatherToCache(repository.getPlace(), gson.toJson(weather));
                });

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
