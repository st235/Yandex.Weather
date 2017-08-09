package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

/**
 * Created by alexander on 12/07/2017.
 */

@MainScope
@InjectViewState
public class NavigationPresenter extends MvpPresenter<NavigationView> {
    private RxSchedulers schedulers;
    private PlacesInteractor placesInteractor;

    @Inject
    public NavigationPresenter(@NonNull RxSchedulers schedulers,
                            @NonNull PlacesInteractor placesInteractor) {
        this.placesInteractor = placesInteractor;
        this.schedulers = schedulers;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        placesInteractor.getFavoritePlaces()
                .compose(schedulers.getComputationToMainTransformerSingle())
                .doOnSuccess(getViewState()::showPlaces)
                .subscribe();
    }
}
