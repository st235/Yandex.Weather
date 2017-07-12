package sasd97.java_blog.xyz.yandexweather.di.modules;

import android.content.Context;
import android.util.SparseArray;

import dagger.Module;
import dagger.Provides;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainPresenter;

/**
 * Created by alexander on 09/07/2017.
 */

@Module
public class MainModule {

    @Provides
    @MainScope
    public SparseArray<String> provideFragmentTags(Context context) {
        SparseArray<String> array = new SparseArray<>();
        array.put(R.id.main_activity_navigation_weather, context.getString(R.string.main_activity_navigation_weather));
        array.put(R.id.main_activity_navigation_about, context.getString(R.string.main_activity_navigation_about));
        array.put(R.id.main_activity_navigation_settings, context.getString(R.string.main_activity_navigation_settings));
        return array;
    }

    @Provides
    @MainScope
    public MainPresenter provideMainPresenter(Context context, SparseArray<String> fragmentTagsConfig) {
        return new MainPresenter(context, fragmentTagsConfig);
    }
}
