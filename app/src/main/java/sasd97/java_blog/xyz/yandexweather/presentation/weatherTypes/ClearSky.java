package sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes;

import java.util.Date;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class ClearSky implements WeatherType {

    /**
     * Only for night.
     */
    static final int CLEAR_SKY_ID = 800;

    @Override
    public boolean isWeatherApplicable(WeatherModel weather) {
        long currentTime = new Date().getTime();
        boolean timeCondition = currentTime < weather.getSunRiseTime() ||
                currentTime >= weather.getSunSetTime();
        return !weather.isForecast() && timeCondition && weather.getWeatherId() == 800;
    }

    @Override
    public boolean isForecastIdApplicable(int forecastId) {
        return false;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_clear_night_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_clear_night_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorClearNightCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorClearNightText;
    }

    @Override
    public int getWeatherId() {
        return CLEAR_SKY_ID;
    }
}
