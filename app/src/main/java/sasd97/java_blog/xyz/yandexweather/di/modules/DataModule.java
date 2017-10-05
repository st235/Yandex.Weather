package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.LocationManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sasd97.java_blog.xyz.yandexweather.data.location.LocationProvider;
import sasd97.java_blog.xyz.yandexweather.data.net.PlacesApi;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;
import sasd97.java_blog.xyz.yandexweather.data.storages.AppDatabase;
import sasd97.java_blog.xyz.yandexweather.data.storages.CacheStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.PlacesDao;
import sasd97.java_blog.xyz.yandexweather.data.storages.PrefsStorage;
import sasd97.java_blog.xyz.yandexweather.data.storages.WeatherDao;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by alexander on 13/07/2017.
 */

@Module
public class DataModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Named("weatherRetrofit")
    @Singleton
    public Retrofit provideWeatherRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(WeatherApi.base_url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Named("placesRetrofit")
    @Singleton
    public Retrofit providePlacesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(PlacesApi.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
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

    @Singleton
    @Provides
    AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    PlacesDao providePlacesDao(AppDatabase database) {
        return database.placesDao();
    }

    @Singleton
    @Provides
    WeatherDao provideWeatherDao(AppDatabase database) {
        return database.weatherDao();
    }

    @Singleton
    @Provides
    LocationManager provideLocationManager(Context context) {
        return (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    @Singleton
    @Provides
    LocationProvider provideLocationProvider(LocationManager locationManager) {
        return new LocationProvider(locationManager);
    }
}
