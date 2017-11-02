package suhockii.dev.weather.presentation.weatherTypes;

import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Thunder implements WeatherType {
    private static final int THUNDER_WEATHER_TYPE = 200;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 2;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId / 100 == 2;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_thunder_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_thunder_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorThunderCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorThunderText;
    }

    @Override
    public int getWeatherId() {
        return THUNDER_WEATHER_TYPE;
    }
}
