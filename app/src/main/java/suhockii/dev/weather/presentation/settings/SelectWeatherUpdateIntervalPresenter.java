package suhockii.dev.weather.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import suhockii.dev.weather.background.UpdateWeatherJob;
import suhockii.dev.weather.di.scopes.MainScope;
import suhockii.dev.weather.domain.settings.SelectWeatherUpdateIntervalInteractor;

/**
 * Created by alexander on 16/07/2017.
 */

@MainScope
@InjectViewState
public class SelectWeatherUpdateIntervalPresenter extends MvpPresenter<SelectWeatherUpdateIntervalView> {

    private SelectWeatherUpdateIntervalInteractor interactor;

    @Inject
    public SelectWeatherUpdateIntervalPresenter(@NonNull SelectWeatherUpdateIntervalInteractor interactor) {
        this.interactor = interactor;
    }

    public int getCurrentIntervalValue() {
        return interactor.getUpdateInterval();
    }

    public void saveCurrentIntervalValue(int minutes) {
        interactor.saveUpdateInterval(minutes);
        UpdateWeatherJob.scheduleJob(getCurrentIntervalValue());
    }
}
