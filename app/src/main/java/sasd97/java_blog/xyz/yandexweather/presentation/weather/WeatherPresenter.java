package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Set;

import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulers;

/**
 * Created by alexander on 12/07/2017.
 */

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    private RxSchedulers schedulers;
    private WeatherInteractor interactor;
    private Set<WeatherType> weatherTypes;

    public WeatherPresenter(@NonNull RxSchedulers schedulers,
                            @NonNull Set<WeatherType> weatherTypes,
                            @NonNull WeatherInteractor interactor) {
        this.interactor = interactor;
        this.schedulers = schedulers;
        this.weatherTypes = weatherTypes;
    }

    @Override
    public void attachView(WeatherView view) {
        super.attachView(view);

        interactor.getWeather(interactor.getCityId())
                .compose(schedulers.getIoToMainTransformer())
                .subscribe(this::chooseWeather);
    }

    public void fetchWeather() {
        interactor.updateWeather(interactor.getCityId())
                .compose(schedulers.getIoToMainTransformer())
                .subscribe(this::chooseWeather);
    }

    public boolean isCelsius() {
        return interactor.getTemperatureUnits() == ConvertersConfig.TEMPERATURE_CELSIUS;
    }

    public boolean isMs() {
        return interactor.getSpeedUnits() == ConvertersConfig.SPEED_MS;
    }

    public boolean isMmHg() {
        return interactor.getPressureUnits() == ConvertersConfig.PRESSURE_MMHG;
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
