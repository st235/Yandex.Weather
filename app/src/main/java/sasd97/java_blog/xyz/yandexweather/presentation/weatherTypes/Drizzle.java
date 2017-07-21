package sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public class Drizzle implements WeatherType {

    @Override
    public boolean isApplicable(WeatherModel weather) {
        return weather.getWeatherId() / 100 == 3;
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
}
