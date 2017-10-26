package suhockii.dev.weather.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import suhockii.dev.weather.navigation.AppActivityRouter;
import suhockii.dev.weather.navigation.Router;
import suhockii.dev.weather.navigation.activities.ActivityCommand;

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
