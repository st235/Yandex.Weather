package sasd97.java_blog.xyz.yandexweather.di.modules;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.converters.Converter;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SelectWeatherUpdateIntervalInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SelectWeatherUpdateIntervalInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractorImpl;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class MainModule {

    @Provides
    @MainScope
    public WeatherInteractor provideWeatherInteractor(Gson gson,
                                                      AppRepository repository,
                                                      Map<String, List<Converter<Integer, Float>>> converters,
                                                      Set<WeatherType> weatherTypes) {
        return new WeatherInteractorImpl(gson, repository, converters, weatherTypes);
    }

    @Provides
    @MainScope
    public PlacesInteractor providePlacesInteractor(AppRepository repository) {
        return new PlacesInteractorImpl(repository);
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
