package sasd97.java_blog.xyz.yandexweather.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.ActivityCommand;
import sasd97.java_blog.xyz.yandexweather.navigation.AppActivityRouter;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class NavigationModule {

    @Provides
    @Singleton
    public Router<ActivityCommand> provideActivitiesRouter() {
        return new AppActivityRouter();
    }
}
