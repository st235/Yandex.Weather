package sasd97.java_blog.xyz.yandexweather.presentation.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

/**
 * Created by alexander on 09/07/2017.
 */

public interface MainView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void showNewFavoritePlace(Place place);
    void closeDrawer();
    void showSuggestions(String[] strings);
}
