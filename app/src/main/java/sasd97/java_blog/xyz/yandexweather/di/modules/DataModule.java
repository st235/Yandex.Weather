package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sasd97.java_blog.xyz.yandexweather.data.net.PlacesApi;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.CacheStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.PrefsStorage;

/**
 * Created by alexander on 13/07/2017.
 */

@Module
public class DataModule {

    @Provides
    @Named("weatherRetrofit")
    @Singleton
    public Retrofit provideWeatherRetrofit(Context context) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .build();
    }

    @Provides
    @Named("placesRetrofit")
    @Singleton
    public Retrofit providePlacesRetrofit(Context context) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .build();
    }

    @Provides
    @Singleton
    public WeatherApi provideWeatherApi(@Named("weatherRetrofit") Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }

    @Provides
    @Singleton
    public PlacesApi providePlacesApi(@Named("placesRetrofit") Retrofit retrofit) {
        return retrofit.create(PlacesApi.class);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    @Provides
    @Singleton
    public CacheStorage provideCacheStorage(Context context) {
        return new CacheStorage(context.getCacheDir());
    }

    @Provides
    @Singleton
    public PrefsStorage providePrefsStorage(Context context) {
        return new PrefsStorage(context);
    }
}
