package suhockii.dev.weather.domain.weather;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TimeZone;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.data.models.forecast.ResponseForecast16;
import suhockii.dev.weather.data.models.forecast.ResponseForecast5;
import suhockii.dev.weather.data.models.forecast.WeatherForecast;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.data.models.weather.ResponseWeather;
import suhockii.dev.weather.domain.converters.Converter;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.presentation.weatherTypes.ClearSky;
import suhockii.dev.weather.presentation.weatherTypes.Cloudy;
import suhockii.dev.weather.presentation.weatherTypes.Sunny;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import timber.log.Timber;

import static suhockii.dev.weather.domain.converters.ConvertersConfig.PRESSURE_CONVERTERS_KEY;
import static suhockii.dev.weather.domain.converters.ConvertersConfig.SPEED_CONVERTERS_KEY;
import static suhockii.dev.weather.domain.converters.ConvertersConfig.TEMPERATURE_CONVERTERS_KEY;

/**
 * Created by alexander on 12/07/2017.
 */

public class WeatherInteractorImpl implements WeatherInteractor {

    private static final String TAG = WeatherInteractorImpl.class.getCanonicalName();
    public static final String WEATHER_NOT_ADDED = "Weather not added";
    public static final String FORECAST_NOT_ADDED = "Forecast not added";

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
    public Single<WeatherModel> getWeather(@NonNull Place place) {
        return Single
                .fromCallable(() -> {
                    String cacheWeather = repository.getCachedWeather(place);
                    if (cacheWeather == null) {
                        throw new NoSuchElementException(WEATHER_NOT_ADDED);
                    }
                    return cacheWeather;
                })
                .map(cache -> gson.fromJson(cache, WeatherModel.class))
                .map(this::convertModel)
                .map(weatherModel -> weatherModel.setCorrectCity(place));
    }

    @Override
    public Single<WeatherModel> updateWeather(@NonNull Place place) {
        return repository.getWeather(place)
                .doOnEvent((weatherModel, throwable) -> repository.saveWeatherToCache(
                        place, gson.toJson(weatherModel)).subscribe())
                .map(this::convertModel)
                .map(weatherModel -> weatherModel.setCorrectCity(place))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<WeatherType>> updateForecast5(@NonNull Place place) {
        return repository.getForecast5(place)
                .doOnError(throwable -> Timber.d(throwable.toString()))
                .map(ResponseForecast5::getForecasts)
                .flatMapIterable(responseForecast5 -> responseForecast5)
                .map(ResponseWeather::getWeatherId)
                .map(this::toWeatherModel)
                .map(this::toWeatherType)
                .toList();
    }

    @Override
    public Single<Map<WeatherModel, WeatherType[]>> updateForecast16(@NonNull Place place) {
        return repository.getForecast16(place)
                .map(ResponseForecast16::getForecasts)
                .flatMapIterable(forecasts -> forecasts)
                .filter(this::isForFuture)
                .map(WeatherForecast::toWeatherModel)
                .map(weatherModel -> addWeatherType(weatherModel, weatherTypes))
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
    public Single<List<WeatherModel>> getForecast(Place place, boolean needUpdate) {
        return repository.getForecast(place != null ? place.getPlaceId() : "", needUpdate)
                .toObservable()
                .flatMapIterable(weatherModels -> weatherModels)
                .map(this::convertModel)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<WeatherModel>> saveForecast(List<WeatherModel> weatherModels) {
        final int[] weatherPos = {0};
        return Observable.fromCallable(() -> weatherModels)
                .flatMapIterable(weatherModels1 -> weatherModels1)
                .doOnNext(weatherModel -> weatherModel.generateUid(weatherPos[0]++))
                .toList()
                .flatMap(weathers -> repository.insertForecast(weathers)
                        .toSingleDefault(weathers));
    }

    @Override
    public Completable removeForecast(String placeId) {
        return repository.removeForecast(placeId);
    }

    public static Pair<WeatherModel, WeatherType[]> addWeatherType(@NonNull WeatherModel weather, Set<WeatherType> weatherTypes) {
        boolean isDetailedForecast = weather.getForecastWeatherIds() != null &&
                weather.getForecastWeatherIds().length == 4;
        WeatherType[] currentWeatherTypes = new WeatherType[isDetailedForecast ? 5 : 1];
        for (WeatherType type : weatherTypes) {
            if (type.isWeatherApplicable(weather)) {
                currentWeatherTypes[0] = type;
            }
            if (!isDetailedForecast) {
                continue;
            }
            if (weather.getForecastWeatherIds()[0] != null) {
                if (type.isForecastIdApplicable(weather.getForecastWeatherIds()[0])) {
                    currentWeatherTypes[1] = type;
                }
            } else {
                currentWeatherTypes[1] = new Cloudy();
            }

            if (weather.getForecastWeatherIds()[1] != null) {
                if (type.isForecastIdApplicable(weather.getForecastWeatherIds()[1])) {
                    currentWeatherTypes[2] = type;
                }
            } else {
                currentWeatherTypes[2] = new Cloudy();
            }

            if (weather.getForecastWeatherIds()[2] != null) {
                if (type.isForecastIdApplicable(weather.getForecastWeatherIds()[2])) {
                    currentWeatherTypes[3] = type;
                }
            } else {
                currentWeatherTypes[3] = new Cloudy();
            }

            if (weather.getForecastWeatherIds()[3] != null) {
                if (type.isForecastIdApplicable(weather.getForecastWeatherIds()[3])) {
                    if (type instanceof Sunny) {
                        currentWeatherTypes[4] = new ClearSky();
                    } else {
                        currentWeatherTypes[4] = type;
                    }
                }
            } else {
                currentWeatherTypes[4] = new Cloudy();
            }

        }
        if (isDetailedForecast) {
            currentWeatherTypes[2] = currentWeatherTypes[0];
        }
        return new Pair<>(weather, currentWeatherTypes);
    }

    private WeatherModel toWeatherModel(int weatherId) {
        return new WeatherModel.Builder().isForecast(true).weatherId(weatherId).build();
    }

    private WeatherType toWeatherType(WeatherModel weather) {
        for (WeatherType type : weatherTypes) {
            if (type.isWeatherApplicable(weather)) {
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

    public WeatherModel convertModel(WeatherModel weather) {
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

    public Set<WeatherType> getWeatherTypes() {
        return weatherTypes;
    }
}
