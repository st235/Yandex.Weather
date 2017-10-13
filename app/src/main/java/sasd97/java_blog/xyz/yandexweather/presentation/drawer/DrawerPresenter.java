package sasd97.java_blog.xyz.yandexweather.presentation.drawer;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.di.scopes.MainScope;
import sasd97.java_blog.xyz.yandexweather.domain.places.PlacesInteractor;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

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
