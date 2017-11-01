package suhockii.dev.weather.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.LocationManager;

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
import suhockii.dev.weather.data.location.LocationProvider;
import suhockii.dev.weather.data.net.PlacesApi;
import suhockii.dev.weather.data.net.WeatherApi;
import suhockii.dev.weather.data.storages.AppDatabase;
import suhockii.dev.weather.data.storages.CacheStorage;
import suhockii.dev.weather.data.storages.PlacesDao;
import suhockii.dev.weather.data.storages.PrefsStorage;
import suhockii.dev.weather.data.storages.WeatherDao;

import static android.content.Context.LOCATION_SERVICE;
import static suhockii.dev.weather.data.storages.AppDatabase.MIGRATION_1_2;

/**
 * Created by alexander on 13/07/2017.
 */

@Module
public class DataModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (BuildConfig.DEBUG) {
//            builder.addNetworkInterceptor(new StethoInterceptor());
//        }
        return builder.build();
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
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
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
