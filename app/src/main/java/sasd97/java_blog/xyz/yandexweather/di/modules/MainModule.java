package sasd97.java_blog.xyz.yandexweather.di.modules;

import java.util.Set;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.models.WeatherType;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherPresenter;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulersAbs;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class MainModule {

    @Provides
    @MainScope
    public MainPresenter provideMainPresenter() {
        return new MainPresenter();
    }

    @Provides
    @MainScope
    public WeatherInteractor provideWeatherInteractor(AppRepository repository) {
        return new WeatherInteractorImpl(repository);
    }

    @Provides
    @MainScope
    public WeatherPresenter provideWeatherPresenter(RxSchedulersAbs schedulers,
                                                    Set<WeatherType> weatherTypes,
                                                    WeatherInteractor interactor) {
        return new WeatherPresenter(schedulers, weatherTypes, interactor);
    }
}
