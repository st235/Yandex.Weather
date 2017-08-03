package sasd97.java_blog.xyz.yandexweather.presentation.cities;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by alexander on 12/07/2017.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface CitiesView extends MvpView {
}
