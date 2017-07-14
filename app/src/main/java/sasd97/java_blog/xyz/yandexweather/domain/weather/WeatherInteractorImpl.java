package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;

/**
 * Created by alexander on 12/07/2017.
 */

public class WeatherInteractorImpl implements WeatherInteractor {

    private AppRepository repository;

    public WeatherInteractorImpl(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<ResponseBody> getWeather(@NonNull String cityId) {
        return repository.getWeather(cityId);
    }
}
