package suhockii.dev.weather.di.modules;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import suhockii.dev.weather.data.AppRepository;
import suhockii.dev.weather.data.AppRepositoryImpl;
import suhockii.dev.weather.data.location.LocationProvider;
import suhockii.dev.weather.data.net.PlacesApi;
import suhockii.dev.weather.data.net.WeatherApi;
import suhockii.dev.weather.data.storages.CacheStorage;
import suhockii.dev.weather.data.storages.PlacesDao;
import suhockii.dev.weather.data.storages.PrefsStorage;
import suhockii.dev.weather.data.storages.WeatherDao;
import suhockii.dev.weather.utils.RxSchedulers;
import suhockii.dev.weather.utils.RxSchedulersImpl;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @NonNull
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public RxSchedulers provideSchedulers() {
        return new RxSchedulersImpl();
    }

    @Provides
    @Singleton
    public Pair<String, String> provideApiKeys() {
        String placesApiKey = null;
        String weatherApiKey = null;
        try {
            weatherApiKey = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("WEATHER_API_KEY");
            placesApiKey = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("PLACES_API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new Pair<>(weatherApiKey, placesApiKey);
    }

    @Provides
    @Singleton
    public AppRepository provideRepository(PlacesDao placesDao,
                                           WeatherDao weatherDao,
                                           WeatherApi weatherApi,
                                           PlacesApi placesApi,
                                           Pair<String, String> apiKeys,
                                           PrefsStorage prefsStorage,
                                           CacheStorage cacheStorage,
                                           LocationProvider locationProvider) {
        return new AppRepositoryImpl(placesDao, weatherDao,
                weatherApi, placesApi, apiKeys, cacheStorage, prefsStorage, locationProvider);
    }
}
