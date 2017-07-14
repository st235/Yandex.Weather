package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;

import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.models.WeatherType;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherView extends MvpView {
    void setWeather(@NonNull WeatherModel weather, @NonNull WeatherType type);
    void stopRefreshing();
}
