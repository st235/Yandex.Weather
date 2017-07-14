package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulersAbs;

/**
 * Created by alexander on 12/07/2017.
 */

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private RxSchedulersAbs schedulers;
    private WeatherInteractor interactor;

    public WeatherPresenter(@NonNull RxSchedulersAbs schedulers,
                            @NonNull WeatherInteractor interactor) {
        this.interactor = interactor;
        this.schedulers = schedulers;
    }

    @Override
    public void attachView(WeatherView view) {
        super.attachView(view);
        view.setSunny();
    }
}
