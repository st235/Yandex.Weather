package suhockii.dev.weather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Map;

import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import suhockii.dev.weather.utils.Settings;

/**
 * Created by alexander on 12/07/2017.
 */
@StateStrategyType(SkipStrategy.class)
public interface WeatherView extends MvpView {
    void showWeather(@NonNull WeatherModel weather, @NonNull WeatherType type);
    void showForecast(Pair<Map<WeatherModel, WeatherType[]>, Settings> pair);

    void stopRefreshing();
    void updateContent();
    void requestEnablingGps(Runnable callingMethod);
    void showGpsSearch();
    void showPlaceName(String placeName);
    void updateWeatherByGps();
    void showError(String msg);
    void hideWeatherAndForecast();
}
