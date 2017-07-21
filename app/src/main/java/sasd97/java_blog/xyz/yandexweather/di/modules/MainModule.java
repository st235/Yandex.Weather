package sasd97.java_blog.xyz.yandexweather.di.modules;

import com.evernote.android.job.JobManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.converters.Converter;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SelectWeatherUpdateIntervalInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SelectWeatherUpdateIntervalInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.settings.SelectWeatherUpdateIntervalPresenter;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.presentation.settings.SettingsPresenter;
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
    public WeatherInteractor provideWeatherInteractor(AppRepository repository,
                                                      Map<String, List<Converter<Integer, Float>>> converters) {
        return new WeatherInteractorImpl(repository, converters);
    }

    @Provides
    @MainScope
    public WeatherPresenter provideWeatherPresenter(RxSchedulersAbs schedulers,
                                                    Set<WeatherType> weatherTypes,
                                                    WeatherInteractor interactor) {
        return new WeatherPresenter(schedulers, weatherTypes, interactor);
    }

    @Provides
    @MainScope
    public SettingsInteractor provideSettingsInteractor(JobManager jobManager,
                                                        AppRepository repository) {
        return new SettingsInteractorImpl(jobManager, repository);
    }

    @Provides
    @MainScope
    public SettingsPresenter provideSettingsPresenter(RxSchedulersAbs schedulers,
                                                      SettingsInteractor interactor) {
        return new SettingsPresenter(schedulers, interactor);
    }

    @Provides
    @MainScope
    public SelectWeatherUpdateIntervalInteractor provideSelectIntervalInteractor(AppRepository repository) {
        return new SelectWeatherUpdateIntervalInteractorImpl(repository);
    }

    @Provides
    @MainScope
    public SelectWeatherUpdateIntervalPresenter provideSelectIntervalPresenter(SelectWeatherUpdateIntervalInteractor interactor) {
        return new SelectWeatherUpdateIntervalPresenter(interactor);
    }
}
