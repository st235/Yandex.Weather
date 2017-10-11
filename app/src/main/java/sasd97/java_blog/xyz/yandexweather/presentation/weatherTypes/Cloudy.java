package sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

import static sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.ClearSky.CLEAR_SKY_ID;

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
