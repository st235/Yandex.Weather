package suhockii.dev.weather.presentation.weatherTypes;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Drizzle implements WeatherType {

    private static final int DRIZZLE_WEATHER_TYPE = 300;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 3;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId / 100 == 3;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_drizzle_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_drizzle_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorDrizzleCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorDrizzleText;
    }

    @Override
    public int getWeatherId() {
        return DRIZZLE_WEATHER_TYPE;
    }
}
