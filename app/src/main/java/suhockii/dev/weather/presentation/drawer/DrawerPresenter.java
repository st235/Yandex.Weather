package suhockii.dev.weather.presentation.drawer;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.di.scopes.MainScope;
import suhockii.dev.weather.domain.places.PlacesInteractor;
import suhockii.dev.weather.utils.RxSchedulers;

/**
 * Created by alexander on 12/07/2017.
 */

@MainScope
@InjectViewState
public class DrawerPresenter extends MvpPresenter<DrawerView> {
    private RxSchedulers schedulers;
    private PlacesInteractor placesInteractor;

    @Inject
    public DrawerPresenter(@NonNull RxSchedulers schedulers,
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
                .subscribe(places -> {}, Throwable::printStackTrace);
    }

    public void removeSelectedPlaces(List<Place> places) {
        placesInteractor.removePlacesFromFavorites(places)
                .compose(schedulers.getIoToMainTransformerCompletable())
                .subscribe(() -> {}, Throwable::printStackTrace);
    }
}
