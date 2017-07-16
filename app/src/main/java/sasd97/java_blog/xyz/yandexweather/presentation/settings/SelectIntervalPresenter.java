package sasd97.java_blog.xyz.yandexweather.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.background.UpdateWeatherJob;
import sasd97.java_blog.xyz.yandexweather.domain.settings.SelectIntervalInteractor;

/**
 * Created by alexander on 16/07/2017.
 */

@InjectViewState
public class SelectIntervalPresenter extends MvpPresenter<SelectIntervalView> {

    private SelectIntervalInteractor interactor;

    public SelectIntervalPresenter(@NonNull SelectIntervalInteractor interactor) {
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
