package suhockii.dev.weather.domain.settings;

import android.support.annotation.NonNull;

import com.evernote.android.job.JobManager;

import suhockii.dev.weather.background.UpdateWeatherJob;
import suhockii.dev.weather.data.AppRepository;

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
}
