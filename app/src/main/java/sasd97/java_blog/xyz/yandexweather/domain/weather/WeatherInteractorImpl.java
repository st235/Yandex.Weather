package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 12/07/2017.
 */

public class WeatherInteractorImpl implements WeatherInteractor {

    private static final String TAG = WeatherInteractorImpl.class.getCanonicalName();

    private AppRepository repository;
    private Gson gson = new Gson();

    public WeatherInteractorImpl(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<WeatherModel> getWeather(@NonNull String cityId) {
        String cacheWeather = repository.getCacheWeather(cityId);
        if (cacheWeather == null)
            return repository
                    .getWeather(cityId)
                    .doOnNext(w -> {
                        Log.e(TAG, w.toString());
                        repository.saveWeather("524901", gson.toJson(w));
                    });

        Log.d(TAG, "Cache is provided.");
        return Observable
                .just(cacheWeather)
                .map(cache -> gson.fromJson(cache, WeatherModel.class));
    }

    @Override
    public Observable<WeatherModel> updateWeather(@NonNull String cityId) {
        return repository
                .getWeather(cityId)
                .doOnNext(w -> {
                    Log.e(TAG, w.toString());
                    repository.saveWeather("5601538", gson.toJson(w));
                });
    }
}
