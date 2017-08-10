package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.WeatherForecast;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.domain.converters.Converter;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.PRESSURE_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.SPEED_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.TEMPERATURE_CONVERTERS_KEY;

/**
 * Created by alexander on 12/07/2017.
 */

public class WeatherInteractorImpl implements WeatherInteractor {

    private static final String TAG = WeatherInteractorImpl.class.getCanonicalName();

    private Gson gson;
    private AppRepository repository;
    private List<Converter<Integer, Float>> speedConverters;
    private List<Converter<Integer, Float>> pressuresConverters;
    private List<Converter<Integer, Float>> temperatureConverters;
    private Set<WeatherType> weatherTypes;

    public WeatherInteractorImpl(@NonNull Gson gson,
                                 @NonNull AppRepository repository,
                                 @NonNull Map<String, List<Converter<Integer, Float>>> converters,
                                 @NonNull Set<WeatherType> weatherTypes) {
        this.gson = gson;
        this.repository = repository;
        this.weatherTypes = weatherTypes;
        this.speedConverters = converters.get(SPEED_CONVERTERS_KEY);
        this.pressuresConverters = converters.get(PRESSURE_CONVERTERS_KEY);
        this.temperatureConverters = converters.get(TEMPERATURE_CONVERTERS_KEY);
    }

    @Override
    public Observable<WeatherModel> getWeather(@NonNull Place place) {
        String cacheWeather = repository.getCachedWeather(place);
        if (cacheWeather == null) return updateWeather(place);
        return Observable.just(cacheWeather)
                .map(cache -> gson.fromJson(cache, WeatherModel.class))
                .map(this::convertModel);
    }

    @Override
    public Observable<WeatherModel> updateWeather(@NonNull Place place) {
        return repository.getWeather(place)
                .doOnNext(w -> repository.saveWeatherToCache(place, gson.toJson(w)))
                .map(this::convertModel);
    }

    @Override
    public Single<ResponseForecast> updateForecast5(@NonNull Place place) {
        return repository.getForecast5(place);
    }

    @Override
    public Single<LinkedHashMap<WeatherModel, WeatherType>> updateForecast16(@NonNull Place place) {
        return repository.getForecast16(place)
                .map(ResponseForecast::getForecasts)
                .flatMapIterable(forecasts -> forecasts)
                .map(WeatherForecast::toWeatherModel)
                .map(this::convertModel)
                .map(this::getWeatherType)
                .collectInto(new LinkedHashMap<>(), (map, pair) -> map.put(pair.first, pair.second));

    }

    private Pair<WeatherModel, WeatherType> getWeatherType(@NonNull WeatherModel weather) {
        for (WeatherType type : weatherTypes) {
            if (type.isApplicable(weather))  return new Pair<>(weather, type);
        }
        return null;
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

    private WeatherModel convertModel(WeatherModel model) {
        return new WeatherModel.Builder(model)
                .temperature(applyConverter(repository.getTemperatureUnits(),
                        model.getTemperature(), temperatureConverters))
                .minTemperature(applyConverter(repository.getTemperatureUnits(),
                        model.getMinTemperature(), temperatureConverters))
                .maxTemperature(applyConverter(repository.getTemperatureUnits(),
                        model.getMaxTemperature(), temperatureConverters))
                .pressure((int) applyConverter(repository.getPressureUnits(),
                        model.getPressure(), pressuresConverters))
                .windSpeed(applyConverter(repository.getSpeedUnits(),
                        model.getWindSpeed(), speedConverters))
                .build();
    }

    private float applyConverter(int mode, float value,
                                 @NonNull List<Converter<Integer, Float>> converters) {
        for (Converter<Integer, Float> converter : converters) {
            if (converter.isApplicable(mode)) return converter.convert(value);
        }
        return value;
    }
}
