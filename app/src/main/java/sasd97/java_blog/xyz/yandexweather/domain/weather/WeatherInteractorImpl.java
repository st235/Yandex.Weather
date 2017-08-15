package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast16;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.ResponseForecast5;
import sasd97.java_blog.xyz.yandexweather.data.models.forecast.WeatherForecast;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.data.models.weather.ResponseWeather;
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
    public Single<List<WeatherType>> updateForecast5(@NonNull Place place) {
        return repository.getForecast5(place)
                .map(ResponseForecast5::getForecasts)
                .flatMapIterable(responseForecast5 -> responseForecast5)
                .map(ResponseWeather::getWeatherId)
                .map(this::toWeatherModel)
                .map(this::toWeatherType)
                .toList();
    }

    @Override
    public Single<LinkedHashMap<WeatherModel, WeatherType[]>> updateForecast16(@NonNull Place place) {
        return repository.getForecast16(place)
                .map(ResponseForecast16::getForecasts)
                .flatMapIterable(forecasts -> forecasts)
                .filter(this::isForFuture)
                .map(WeatherForecast::toWeatherModel)
                .map(this::convertModel)
                .map(this::addWeatherType)
                .collectInto(new LinkedHashMap<>(), (map, pair) -> map.put(pair.first, pair.second));
                /*pair.first is weather, pair.second is weather type*/
    }

    private boolean isForFuture(WeatherForecast weatherForecast) {
        String current = Instant.now().atZone(ZoneId.of(TimeZone.getDefault().getID()))
                .format(DateTimeFormatter.BASIC_ISO_DATE).split("\\+")[0];
        Instant instantForecast = Instant.ofEpochSecond(weatherForecast.getUpdateTime());
        String date = instantForecast.atZone(ZoneId.of(TimeZone.getDefault().getID()))
                .format(DateTimeFormatter.BASIC_ISO_DATE).split("\\+")[0];
        return !current.equals(date) && Instant.now().isBefore(instantForecast);
    }

    @Override
    public Single<LinkedHashMap<WeatherModel, WeatherType[]>> getForecast(String placeId) {
        return repository.getForecast(placeId)
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .map(this::addWeatherType)
                .collectInto(new LinkedHashMap<>(), (map, pair) -> map.put(pair.first, pair.second));
    }

    @Override
    public Completable saveForecast(List<WeatherModel> forecast) {
        return repository.insertForecast(forecast);
    }

    @Override
    public Completable removeForecast(String placeId) {
        return repository.removeForecast(placeId);
    }

    private Pair<WeatherModel, WeatherType[]> addWeatherType(@NonNull WeatherModel weather) {
        for (WeatherType type : weatherTypes) {
            if (type.isApplicable(weather)) {
                WeatherType[] weatherTypes = new WeatherType[4];
                weatherTypes[0] = type;
                return new Pair<>(weather, weatherTypes);
            }
        }
        throw new IllegalStateException("Should not get here");
    }

    private WeatherModel toWeatherModel(int weatherId) {
        return new WeatherModel.Builder().isForecast(true).weatherId(weatherId).build();
    }

    private WeatherType toWeatherType(WeatherModel weather) {
        for (WeatherType type : weatherTypes) {
            if (type.isApplicable(weather)) {
                return type;
            }
        }
        throw new IllegalStateException("Should not get here");
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

    private WeatherModel convertModel(WeatherModel weather) {
        return new WeatherModel.Builder(weather)
                .temperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getTemperature(), temperatureConverters))
                .morningTemperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getMorningTemperature(), temperatureConverters))
                .dayTemperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getDayTemperature(), temperatureConverters))
                .eveningTemperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getEveningTemperature(), temperatureConverters))
                .nightTemperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getNightTemperature(), temperatureConverters))
                .minTemperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getMinTemperature(), temperatureConverters))
                .maxTemperature(applyConverter(repository.getTemperatureUnits(),
                        weather.getMaxTemperature(), temperatureConverters))
                .pressure((int) applyConverter(repository.getPressureUnits(),
                        weather.getPressure(), pressuresConverters))
                .windSpeed(applyConverter(repository.getSpeedUnits(),
                        weather.getWindSpeed(), speedConverters))
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
