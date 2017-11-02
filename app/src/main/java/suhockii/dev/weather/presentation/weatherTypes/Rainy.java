package suhockii.dev.weather.presentation.weatherTypes;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Rainy implements WeatherType {
    private static final int RAINY_WEATHER_TYPE = 500;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 5;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId / 100 == 5;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_rainy_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_rainy_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorRainyCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorRainyText;
    }

    @Override
    public int getWeatherId() {
        return RAINY_WEATHER_TYPE;
    }
}
