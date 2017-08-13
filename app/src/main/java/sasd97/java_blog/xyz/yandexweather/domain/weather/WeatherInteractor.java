package sasd97.java_blog.xyz.yandexweather.domain.weather;

import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherInteractor {
    Observable<WeatherModel> getWeather(@NonNull Place place);
    Observable<WeatherModel> updateWeather(@NonNull Place place);

    Single<List<WeatherType>> updateForecast5(@NonNull Place place);
    Single<LinkedHashMap<WeatherModel, WeatherType[]>> updateForecast16(@NonNull Place place);

    //db
    Single<List<WeatherModel>> getForecast(String placeId);
    Completable saveForecast(List<WeatherModel> forecast);
    Completable removeForecast(String placeId);

    int getTemperatureUnits();
    int getPressureUnits();
    int getSpeedUnits();
}
