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

    private Gson gson = new Gson();
    private AppRepository repository;

    public UpdateWeatherJob(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Log.i(TAG, "Update is started");

        repository
                .getWeather(repository.getCity())
                .doOnError(Throwable::printStackTrace)
                .subscribe(weather -> {
                    Log.i(TAG, weather.toString());
                    repository.saveWeatherToCache(repository.getCity(), gson.toJson(weather));
                });

        return Result.SUCCESS;
    }

    public static void scheduleJob(int minutes) {
        new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(minutes), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }
}
