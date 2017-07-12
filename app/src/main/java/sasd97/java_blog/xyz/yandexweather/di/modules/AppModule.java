package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.net.WeatherApi;

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
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public SparseArray<String> provideFragmentTags(Context context) {
        SparseArray<String> array = new SparseArray<>();
        array.put(R.id.main_activity_navigation_weather, context.getString(R.string.main_activity_navigation_weather));
        array.put(R.id.main_activity_navigation_about, context.getString(R.string.main_activity_navigation_about));
        array.put(R.id.main_activity_navigation_settings, context.getString(R.string.main_activity_navigation_settings));
        return array;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .build();
    }

    @Provides
    @Singleton
    public WeatherApi provideApi(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }
}
