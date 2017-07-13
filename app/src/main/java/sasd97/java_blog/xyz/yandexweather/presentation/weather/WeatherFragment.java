package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;

/**
 * Created by alexander on 09/07/2017.
 */

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView {

    @InjectPresenter WeatherPresenter presenter;

    @ProvidePresenter
    public WeatherPresenter providePresenter() {
        return WeatherApp
                .get(getContext())
                .getMainComponent()
                .getWeatherPresenter();
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadWeather();

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(R.string.main_activity_navigation_weather);
    }
}
