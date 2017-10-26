package suhockii.dev.weather.di.modules;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import suhockii.dev.weather.presentation.weatherTypes.ClearSky;
import suhockii.dev.weather.presentation.weatherTypes.Cloudy;
import suhockii.dev.weather.presentation.weatherTypes.Drizzle;
import suhockii.dev.weather.presentation.weatherTypes.Foggy;
import suhockii.dev.weather.presentation.weatherTypes.Rainy;
import suhockii.dev.weather.presentation.weatherTypes.Snowy;
import suhockii.dev.weather.presentation.weatherTypes.Sunny;
import suhockii.dev.weather.presentation.weatherTypes.Thunder;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;

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
