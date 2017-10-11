package sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Foggy implements WeatherType {
    private static final int FOGGY_WEATHER_TYPE = 741;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 7;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return forecastId / 100 == 7;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_foggy_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_foggy_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorFoggyCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorFoggyText;
    }

    @Override
    public int getWeatherId() {
        return FOGGY_WEATHER_TYPE;
    }
}
