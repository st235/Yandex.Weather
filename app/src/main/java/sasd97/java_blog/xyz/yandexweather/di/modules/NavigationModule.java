package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.AppActivityRouter;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.keepers.AppFragmentRouterKeeper;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.keepers.RouterKeeper;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class NavigationModule {

    @Provides
    @Singleton
    public Router<Integer> provideActivitiesRouter(Context context, SparseArray<Class<?>> activitiesMap) {
        return new AppActivityRouter(context, activitiesMap);
    }

    @Provides
    @Singleton
    public RouterKeeper<Fragment> provideFragmentRouterKeeper() {
        return new AppFragmentRouterKeeper();
    }
}
