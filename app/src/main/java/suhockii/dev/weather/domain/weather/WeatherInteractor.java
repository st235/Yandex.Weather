package suhockii.dev.weather.domain.weather;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Single;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherInteractor {
    Single<WeatherModel> getWeather(@NonNull Place place);
    Single<WeatherModel> updateWeather(@NonNull Place place);

    Single<List<WeatherType>> updateForecast5(@NonNull Place place);
    Single<Map<WeatherModel, WeatherType[]>> updateForecast16(@NonNull Place place);

    //db
    Single<List<WeatherModel>> getForecast(Place place, boolean needUpdate);
    Single<List<WeatherModel>> saveForecast(List<WeatherModel> weatherModels);
    Completable removeForecast(String placeId);

    int getTemperatureUnits();
    int getPressureUnits();
    int getSpeedUnits();
    WeatherModel convertModel(WeatherModel weather);

    Set<WeatherType> getWeatherTypes();
}
