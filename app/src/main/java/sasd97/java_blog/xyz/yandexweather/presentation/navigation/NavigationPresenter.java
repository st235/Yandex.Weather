package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;

/**
 * Created by alexander on 12/07/2017.
 */

@MainScope
@InjectViewState
public class NavigationPresenter extends MvpPresenter<NavigationView> {
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showPlaces(new ArrayList<>());
    }
}
