package sasd97.java_blog.xyz.yandexweather.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.domain.settings.SettingsInteractor;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulersAbs;

/**
 * Created by alexander on 15/07/2017.
 */

public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private RxSchedulersAbs schedulers;
    private SettingsInteractor interactor;

    public SettingsPresenter(@NonNull RxSchedulersAbs schedulers,
                             @NonNull SettingsInteractor interactor) {
        this.schedulers = schedulers;
        this.interactor = interactor;
    }
}
