package suhockii.dev.weather.di;

import dagger.Subcomponent;
import suhockii.dev.weather.di.modules.ConvertersModule;
import suhockii.dev.weather.di.modules.MainModule;
import suhockii.dev.weather.di.modules.WeatherTypesModule;
import suhockii.dev.weather.di.scopes.MainScope;
import suhockii.dev.weather.presentation.drawer.DrawerPresenter;
import suhockii.dev.weather.presentation.main.MainActivity;
import suhockii.dev.weather.presentation.main.MainPresenter;
import suhockii.dev.weather.presentation.settings.SelectWeatherUpdateIntervalPresenter;
import suhockii.dev.weather.presentation.settings.SettingsPresenter;
import suhockii.dev.weather.presentation.weather.WeatherPresenter;

/**
 * Created by alexander on 09/07/2017.
 */

@Subcomponent(modules = {MainModule.class, ConvertersModule.class, WeatherTypesModule.class})
@MainScope
public interface MainComponent {
    void inject(MainActivity activity);

    MainPresenter getMainPresenter();

    WeatherPresenter getWeatherPresenter();

    DrawerPresenter getNavigationPresenter();

    SettingsPresenter getSettingsPresenter();

    SelectWeatherUpdateIntervalPresenter getSelectIntervalPresenter();
}
