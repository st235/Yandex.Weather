package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by alexander on 12/07/2017.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherView extends MvpView {
    void setWeather(@NonNull WeatherModel weather, @NonNull WeatherType type);
    void stopRefreshing();
}
