package sasd97.java_blog.xyz.yandexweather.domain.settings;

import android.support.annotation.NonNull;

import com.evernote.android.job.JobManager;

import io.reactivex.Completable;
import sasd97.java_blog.xyz.yandexweather.background.UpdateWeatherJob;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

/**
 * Created by alexander on 15/07/2017.
 */

public class SettingsInteractorImpl implements SettingsInteractor {

    private JobManager jobManager;
    private AppRepository repository;

    public SettingsInteractorImpl(@NonNull JobManager jobManager,
                                  @NonNull AppRepository repository) {
        this.jobManager = jobManager;
        this.repository = repository;
    }

    @Override
    public boolean isBackgroundServiceEnabled() {
        return repository.isBackgroundServiceEnabled();
    }

    @Override
    public boolean switchBackgroundServiceState() {
        boolean state = repository.switchBackgroundServiceState();

        if (state) {
            UpdateWeatherJob.scheduleJob(repository.getWeatherUpdateInterval());
        } else if (jobManager != null){
            jobManager.cancelAllForTag(UpdateWeatherJob.TAG);
        }

        return state;
    }

    @Override
    public int getUpdateInterval() {
        return repository.getWeatherUpdateInterval();
    }

    @Override
    public void saveTemperatureUnits(int units) {
        repository.saveTemperatureUnits(units);
    }

    @Override
    public int getTemperatureUnits() {
        return repository.getTemperatureUnits();
    }

    @Override
    public void savePressureUnits(int units) {
        repository.savePressureUnits(units);
    }

    @Override
    public int getPressureUnits() {
        return repository.getPressureUnits();
    }

    @Override
    public void saveSpeedUnits(int units) {
        repository.saveSpeedUnits(units);
    }

    @Override
    public int getSpeedUnits() {
        return repository.getSpeedUnits();
    }

    @Override
    public Completable savePlace(@NonNull Place place) {
        return repository.savePlace(place);
    }
}
