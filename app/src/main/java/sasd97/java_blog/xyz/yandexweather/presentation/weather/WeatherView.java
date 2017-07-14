package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import com.arellomobile.mvp.MvpView;

/**
 * Created by alexander on 12/07/2017.
 */

public interface WeatherView extends MvpView {
    void setSunny();
    void setClearSky();
    void setFoggy();
    void setCloudy();
    void setRainy();
    void setSnowy();
    void setThunder();
    void setDrizzle();
}
