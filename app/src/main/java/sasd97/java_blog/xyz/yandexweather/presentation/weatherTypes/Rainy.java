package sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Rainy implements WeatherType {

    @Override
    public boolean isApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 5;
    }

    @Override
    public int getIconRes() {
        return R.string.all_weather_rainy_icon;
    }

    @Override
    public int getTitleRes() {
        return R.string.all_weather_rainy_title;
    }

    @Override
    public int getCardColor() {
        return R.color.colorRainyCard;
    }

    @Override
    public int getTextColor() {
        return R.color.colorRainyText;
    }
}
