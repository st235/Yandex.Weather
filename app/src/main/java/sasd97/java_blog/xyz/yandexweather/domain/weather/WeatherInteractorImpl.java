package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.domain.converters.Converter;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.PRESSURE_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.SPEED_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.TEMPERATURE_CONVERTERS_KEY;

/**
 * Created by alexander on 12/07/2017.
 */

public class WeatherInteractorImpl implements WeatherInteractor {

    private static final String TAG = WeatherInteractorImpl.class.getCanonicalName();

    private Gson gson = new Gson();
    private AppRepository repository;
    private List<Converter<Integer, Float>> speedConverters;
    private List<Converter<Integer, Float>> pressuresConverters;
    private List<Converter<Integer, Float>> temperatureConverters;

    public WeatherInteractorImpl(@NonNull AppRepository repository,
                                 @NonNull Map<String, List<Converter<Integer, Float>>> converters) {
        this.repository = repository;
        this.speedConverters = converters.get(SPEED_CONVERTERS_KEY);
        this.pressuresConverters = converters.get(PRESSURE_CONVERTERS_KEY);
        this.temperatureConverters = converters.get(TEMPERATURE_CONVERTERS_KEY);
    }

    @NonNull
    @Override
    public String getCityId() {
        return repository.getCity();
    }

    @Override
    public Observable<WeatherModel> getWeather(@NonNull String cityId) {
        String cacheWeather = repository.getCachedWeather(cityId);
        if (cacheWeather == null) return updateWeather(cityId);

        Log.i(TAG, "Cache provided.");

        Observable<WeatherModel> observable = Observable.just(cacheWeather)
                .map(cache -> gson.fromJson(cache, WeatherModel.class));

        return convertModel(observable);
    }

    @Override
    public Observable<WeatherModel> updateWeather(@NonNull String cityId) {
        Observable<WeatherModel> observable = repository
                .getWeather(cityId)
                .doOnNext(w -> repository.saveWeatherToCache(cityId, gson.toJson(w)));
        return convertModel(observable);
    }

    @Override
    public int getTemperatureUnits() {
        return repository.getTemperatureUnits();
    }

    @Override
    public int getPressureUnits() {
        return repository.getPressureUnits();
    }

    @Override
    public int getSpeedUnits() {
        return repository.getSpeedUnits();
    }

    private Observable<WeatherModel> convertModel(Observable<WeatherModel> weatherObservable) {
        return weatherObservable
                .map(w -> new WeatherModel.Builder(w)
                        .temperature(
                                applyConverter(repository.getTemperatureUnits(),
                                        w.getTemperature(), temperatureConverters)
                        )
                        .minTemperature(
                                applyConverter(repository.getTemperatureUnits(),
                                        w.getMinTemperature(), temperatureConverters)
                        )
                        .maxTemperature(
                                applyConverter(repository.getTemperatureUnits(),
                                        w.getMaxTemperature(), temperatureConverters)
                        )
                        .pressure(
                                (int) applyConverter(repository.getPressureUnits(),
                                        w.getPressure(), pressuresConverters)
                        )
                        .windSpeed(
                                applyConverter(repository.getSpeedUnits(),
                                        w.getWindSpeed(), speedConverters)
                        )
                        .build());
    }

    private float applyConverter(int mode, float value,
                                 @NonNull List<Converter<Integer, Float>> converters) {
        for (Converter<Integer, Float> converter: converters) {
            if (converter.isApplicable(mode)) return converter.convert(value);
        }
        return value;
    }
}
