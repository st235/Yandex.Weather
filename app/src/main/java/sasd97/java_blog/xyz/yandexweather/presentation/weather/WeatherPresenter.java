package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Set;

import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.models.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulersAbs;

/**
 * Created by alexander on 12/07/2017.
 */

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private static final String TAG = WeatherPresenter.class.getCanonicalName();

    private RxSchedulersAbs schedulers;
    private WeatherInteractor interactor;
    private Set<WeatherType> weatherTypes;

    public WeatherPresenter(@NonNull RxSchedulersAbs schedulers,
                            @NonNull Set<WeatherType> weatherTypes,
                            @NonNull WeatherInteractor interactor) {
        this.interactor = interactor;
        this.schedulers = schedulers;
        this.weatherTypes = weatherTypes;
    }

    @Override
    public void attachView(WeatherView view) {
        super.attachView(view);

        interactor.getWeather("5601538")
                .compose(schedulers.getIOToMainTransformer())
                .subscribe(this::chooseWeather);
    }

    public void fetchWeather() {
        interactor.updateWeather("5601538")
                .compose(schedulers.getIOToMainTransformer())
                .subscribe(this::chooseWeather);
    }

    private void chooseWeather(@NonNull WeatherModel weather) {
        for(WeatherType type: weatherTypes) {
            if (!type.isApplicable(weather)) continue;
            getViewState().setWeather(weather, type);
            break;
        }

        getViewState().stopRefreshing();
    }
}
