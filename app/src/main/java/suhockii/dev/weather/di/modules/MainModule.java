package suhockii.dev.weather.di.modules;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.di.scopes.MainScope;
import suhockii.dev.weather.domain.converters.Converter;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.domain.places.PlacesInteractorImpl;
import suhockii.dev.weather.domain.settings.SelectWeatherUpdateIntervalInteractor;
import suhockii.dev.weather.domain.settings.SelectWeatherUpdateIntervalInteractorImpl;
import suhockii.dev.weather.domain.settings.SettingsInteractor;
import suhockii.dev.weather.domain.settings.SettingsInteractorImpl;
import suhockii.dev.weather.domain.weather.WeatherInteractor;
import suhockii.dev.weather.domain.weather.WeatherInteractorImpl;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;

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
