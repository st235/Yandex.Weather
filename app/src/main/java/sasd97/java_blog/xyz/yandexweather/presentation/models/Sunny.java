package sasd97.java_blog.xyz.yandexweather.presentation.models;

import java.util.Date;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Sunny implements WeatherType {

    @Override
    public boolean isApplicable(WeatherModel weather) {
        long currentTime = new Date().getTime();

        boolean timeCondition =
                currentTime >= weather.getSunRiseTime() && currentTime < weather.getSunSetTime();

        return timeCondition && weather.getWeatherId() == 800;
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
}
