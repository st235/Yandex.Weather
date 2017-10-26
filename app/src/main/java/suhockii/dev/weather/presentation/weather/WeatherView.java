package suhockii.dev.weather.presentation.weather;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Map;

import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import suhockii.dev.weather.utils.Settings;

/**
 * Created by alexander on 12/07/2017.
 */
@StateStrategyType(SkipStrategy.class)
public interface WeatherView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showWeather(@NonNull WeatherModel weather, @NonNull WeatherType type);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showForecast(Pair<Map<WeatherModel, WeatherType[]>, Settings> pair);

    void stopRefreshing();
    void updateContent();
    void requestEnablingGps(Runnable callingMethod);
    void showGpsSearch();
    void stopGpsAnimation(Place place);
    void updateWeatherByGps();
}
