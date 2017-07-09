package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;
import android.util.SparseArray;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.presentation.MainActivity;
import sasd97.java_blog.xyz.yandexweather.presentation.SplashScreenActivity;

import static sasd97.java_blog.xyz.yandexweather.navigation.AppActivityRouter.SPLASH_SCREEN_ACTIVITY;
import static sasd97.java_blog.xyz.yandexweather.navigation.AppActivityRouter.WEATHER_ACTIVITY;

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
    public SparseArray<Class<?>> provideActivitiesMap() {
        SparseArray<Class<?>> activitiesMap = new SparseArray<>();
        activitiesMap.put(SPLASH_SCREEN_ACTIVITY, SplashScreenActivity.class);
        activitiesMap.put(WEATHER_ACTIVITY, MainActivity.class);
        return activitiesMap;
    }
}
