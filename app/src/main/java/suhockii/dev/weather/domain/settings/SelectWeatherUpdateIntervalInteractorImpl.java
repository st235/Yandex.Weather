package suhockii.dev.weather.domain.settings;

import android.support.annotation.NonNull;

import suhockii.dev.weather.data.AppRepository;

/**
 * Created by alexander on 16/07/2017.
 */

public class SelectWeatherUpdateIntervalInteractorImpl implements SelectWeatherUpdateIntervalInteractor {

    public AppRepository repository;

    public SelectWeatherUpdateIntervalInteractorImpl(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveUpdateInterval(int minutes) {
        repository.saveWeatherUpdateInterval(minutes);
    }

    @Override
    public int getUpdateInterval() {
        return repository.getWeatherUpdateInterval();
    }
}
