package sasd97.java_blog.xyz.yandexweather.domain.settings;

import android.support.annotation.NonNull;

import sasd97.java_blog.xyz.yandexweather.data.AppRepository;

/**
 * Created by alexander on 16/07/2017.
 */

public class SelectIntervalInteractorImpl implements SelectIntervalInteractor {

    public AppRepository repository;

    public SelectIntervalInteractorImpl(@NonNull AppRepository repository) {
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
