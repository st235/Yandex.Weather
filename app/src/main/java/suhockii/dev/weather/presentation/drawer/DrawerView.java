package suhockii.dev.weather.presentation.drawer;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import suhockii.dev.weather.data.models.places.Place;

/**
 * Created by alexander on 12/07/2017.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface DrawerView extends MvpView {
    void showPlaces(List<Place> places);
}
