package sasd97.java_blog.xyz.yandexweather.di.modules;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import sasd97.java_blog.xyz.yandexweather.presentation.models.ClearSky;
import sasd97.java_blog.xyz.yandexweather.presentation.models.Drizzle;
import sasd97.java_blog.xyz.yandexweather.presentation.models.Foggy;
import sasd97.java_blog.xyz.yandexweather.presentation.models.Rainy;
import sasd97.java_blog.xyz.yandexweather.presentation.models.Snowy;
import sasd97.java_blog.xyz.yandexweather.presentation.models.Sunny;
import sasd97.java_blog.xyz.yandexweather.presentation.models.Thunder;
import sasd97.java_blog.xyz.yandexweather.presentation.models.WeatherType;

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
