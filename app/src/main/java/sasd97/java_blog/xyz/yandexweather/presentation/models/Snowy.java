package sasd97.java_blog.xyz.yandexweather.presentation.models;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Snowy implements WeatherType {

    @Override
    public boolean isApplicable(WeatherModel weather) {
        return weather.getWeatherId() == 600;
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
}
