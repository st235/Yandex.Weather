package suhockii.dev.weather.presentation.weatherTypes;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Snowy implements WeatherType {
    private static final int SNOWY_WEATHER_TYPE = 600;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 6;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId / 100 == 6;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_snowy_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_snowy_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorSnowyCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorSnowyText;
    }

    @Override
    public int getWeatherId() {
        return SNOWY_WEATHER_TYPE;
    }
}
