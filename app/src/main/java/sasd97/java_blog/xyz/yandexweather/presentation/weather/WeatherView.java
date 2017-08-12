package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.LinkedHashMap;

import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

/**
 * Created by alexander on 12/07/2017.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherView extends MvpView {
    void setWeather(@NonNull WeatherModel weather, @NonNull WeatherType type);
    void stopRefreshing();
    void showForecast(Pair<LinkedHashMap<WeatherModel, WeatherType[]>, Settings> pair);
}
