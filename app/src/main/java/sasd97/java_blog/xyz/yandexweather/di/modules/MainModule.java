package sasd97.java_blog.xyz.yandexweather.di.modules;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

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
import sasd97.java_blog.xyz.yandexweather.presentation.search.SearchPresenter;

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
    public SearchPresenter provideSearchPresenter() {
        return new SearchPresenter();
    }

    @Provides
    @MainScope
    public WeatherInteractor provideWeatherInteractor(Gson gson,
                                                      AppRepository repository,
                                                      Map<String, List<Converter<Integer, Float>>> converters) {
        return new WeatherInteractorImpl(gson, repository, converters);
    }

    @Provides
    @MainScope
    public SettingsInteractor provideSettingsInteractor(JobManager jobManager,
                                                        AppRepository repository) {
        return new SettingsInteractorImpl(jobManager, repository);
    }

    @Provides
    @MainScope
    public SelectWeatherUpdateIntervalInteractor provideSelectIntervalInteractor(AppRepository repository) {
        return new SelectWeatherUpdateIntervalInteractorImpl(repository);
    }
}
