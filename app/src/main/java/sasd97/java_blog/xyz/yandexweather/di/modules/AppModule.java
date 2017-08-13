package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.data.AppRepository;
import sasd97.java_blog.xyz.yandexweather.data.AppRepositoryImpl;
import sasd97.java_blog.xyz.yandexweather.data.net.PlacesApi;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.CacheStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.PlacesDao;
import sasd97.java_blog.xyz.yandexweather.data.storages.PrefsStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.WeatherDao;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulersImpl;

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
                                           CacheStorage cacheStorage) {
        return new AppRepositoryImpl(placesDao, weatherDao,
                weatherApi, placesApi, apiKeys, cacheStorage, prefsStorage);
    }
}
