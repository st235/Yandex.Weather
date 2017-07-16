package sasd97.java_blog.xyz.yandexweather.di.modules;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.ClearSky;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Cloudy;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Drizzle;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Foggy;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Rainy;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Snowy;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Sunny;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.Thunder;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by alexander on 14/07/2017.
 */

@Module
public class WeatherTypesModule {

    @Provides
    @IntoSet
    public WeatherType provideSunny() {
        return new Sunny();
    }

    @Provides
    @IntoSet
    public WeatherType provideClearSky() {
        return new ClearSky();
    }

    @Provides
    @IntoSet
    public WeatherType provideCloudy() {
        return new Cloudy();
    }

    @Provides
    @IntoSet
    public WeatherType provideThunder() {
        return new Thunder();
    }

    @Provides
    @IntoSet
    public WeatherType provideDrizzle() {
        return new Drizzle();
    }

    @Provides
    @IntoSet
    public WeatherType provideRainy() {
        return new Rainy();
    }

    @Provides
    @IntoSet
    public WeatherType provideSnowy() {
        return new Snowy();
    }

    @Provides
    @IntoSet
    public WeatherType provideFoggy() {
        return new Foggy();
    }
}
