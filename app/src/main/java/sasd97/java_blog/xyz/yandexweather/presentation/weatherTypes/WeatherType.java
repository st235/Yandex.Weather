package sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;

/**
 * Created by alexander on 14/07/2017.
 */

public interface WeatherType {
    boolean isApplicable(WeatherModel weather);

    @StringRes int getIconRes();
    @StringRes int getTitleRes();
    @ColorRes int getCardColor();
    @ColorRes int getTextColor();
}
