package suhockii.dev.weather.presentation.weatherTypes;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import suhockii.dev.weather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public interface WeatherType {
    boolean isWeatherApplicable(WeatherModel weather);
    boolean isForecastIdApplicable(int forecastId);

    @StringRes int getIconRes();
    @StringRes int getTitleRes();
    @ColorRes int getCardColor();
    @ColorRes int getTextColor();
    int getWeatherId();
}
