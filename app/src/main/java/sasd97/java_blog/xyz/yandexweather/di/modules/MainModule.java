package sasd97.java_blog.xyz.yandexweather.di.modules;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainPresenter;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class MainModule {

    @Provides
    @MainScope
    public MainPresenter provideMainPresenter() {
        return new MainPresenter();
    }
}
