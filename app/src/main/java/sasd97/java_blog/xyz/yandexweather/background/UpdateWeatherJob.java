package sasd97.java_blog.xyz.yandexweather.background;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import sasd97.java_blog.xyz.yandexweather.data.AppRepository;

/**
 * Created by alexander on 13/07/2017.
 */

public class UpdateWeatherJob extends Job {

    public static final String TAG = "job.weather.update";

    private AppRepository repository;

    public UpdateWeatherJob(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }
}
