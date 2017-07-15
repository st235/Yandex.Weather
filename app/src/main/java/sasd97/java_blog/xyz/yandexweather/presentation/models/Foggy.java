package sasd97.java_blog.xyz.yandexweather.presentation.models;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Foggy implements WeatherType {

    @Override
    public boolean isApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 7;
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
}
