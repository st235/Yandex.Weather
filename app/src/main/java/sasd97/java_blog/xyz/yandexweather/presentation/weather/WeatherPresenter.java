package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.ResponseWeather;
import sasd97.java_blog.xyz.yandexweather.domain.weather.WeatherInteractor;
import sasd97.java_blog.xyz.yandexweather.utils.RxSchedulersAbs;

/**
 * Created by alexander on 12/07/2017.
 */

@InjectViewState
public class WeatherPresenter extends MvpPresenter<MvpView> {

    private Context context;
    private RxSchedulersAbs schedulers;
    private WeatherInteractor interactor;

    public WeatherPresenter(@NonNull Context context,
                            @NonNull RxSchedulersAbs schedulers,
                            @NonNull WeatherInteractor interactor) {
        this.context = context;
        this.interactor = interactor;
        this.schedulers = schedulers;
    }

    public void loadWeather() {
        interactor
        .getWeather("5601538", context.getString(R.string.open_weather_api_key))
        .compose(schedulers.getIOToMainTransformer())
        .onErrorReturn(t -> {
            t.printStackTrace();
            return new ResponseWeather();
        })
        .subscribe(w -> {
            Log.d("TAG", w.toString());
        });
    }
}
