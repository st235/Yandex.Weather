package sasd97.java_blog.xyz.yandexweather.presentation.main;

import com.arellomobile.mvp.MvpView;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Places;

/**
 * Created by alexander on 09/07/2017.
 */

public interface MainView extends MvpView {
    void closeDrawer();
    void selectNavigationItem(int id);
    void showSuggestions(Places places);
}
