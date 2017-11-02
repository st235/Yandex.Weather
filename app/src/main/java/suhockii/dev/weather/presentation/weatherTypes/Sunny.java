package suhockii.dev.weather.presentation.weatherTypes;

import java.util.Date;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Sunny implements WeatherType {
    private static final int SUNNY_WEATHER_TYPE = 800;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        long currentTime = new Date().getTime();

        boolean timeCondition = currentTime >= weather.getSunRiseTime() &&
                currentTime < weather.getSunSetTime();

        return (timeCondition || weather.isForecast()) && weather.getWeatherId() == 800;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId == 800;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_sunny_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_sunny_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorSunnyCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorSunnyText;
    }

    @Override
    public int getWeatherId() {
        return SUNNY_WEATHER_TYPE;
    }
}
