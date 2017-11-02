package suhockii.dev.weather.presentation.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import suhockii.dev.weather.data.models.places.Place;

/**
 * Created by alexander on 09/07/2017.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void showNewFavoritePlace(Place place);

    void closeDrawer();

    void showSuggestions(String[] suggests);

    @StateStrategyType(SkipStrategy.class)
    void updateWeatherContent();

    @StateStrategyType(SkipStrategy.class)
    void closeActivity();
}
