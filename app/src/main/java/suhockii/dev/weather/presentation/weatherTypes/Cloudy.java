package suhockii.dev.weather.presentation.weatherTypes;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.models.WeatherModel;

import static suhockii.dev.weather.presentation.weatherTypes.ClearSky.CLEAR_SKY_ID;

/**
 * Created by alexander on 14/07/2017.
 */

public class Cloudy implements WeatherType {

    private static final int CLOUDY_WEATHER_TYPE = 801;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        return weather.getWeatherId() != CLEAR_SKY_ID && weather.getWeatherId() / 100 == 8;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId != CLEAR_SKY_ID && forecastId / 100 == 8;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_cloudy_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_cloudy_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorCloudyCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorCloudyText;
    }

    @Override
    public int getWeatherId() {
        return CLOUDY_WEATHER_TYPE;
    }
}
