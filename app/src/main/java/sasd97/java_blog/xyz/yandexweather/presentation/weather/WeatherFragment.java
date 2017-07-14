package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;

/**
 * Created by alexander on 09/07/2017.
 */

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView {

    @BindView(R.id.fragment_weather_icon) TextView weatherIcon;
    @BindView(R.id.fragment_weather_city) TextView weatherCity;
    @BindView(R.id.fragment_weather_type) TextView weatherType;
    @BindView(R.id.fragment_weather_card) CardView weatherCard;
    @BindView(R.id.fragment_weather_delimiter) View weatherDelimiter;
    @BindView(R.id.fragment_weather_temperature) TextView weatherTemperature;
    @BindView(R.id.fragment_weather_swipe_to_refresh) SwipeRefreshLayout swipeRefreshLayout;

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
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimaryDark, R.color.colorPrimary);

        weatherIcon.setText(R.string.all_weather_sunny);
    }

    @Override
    public void setSunny() {
        updateWeatherTheme(R.color.colorSunnyCard, R.color.colorSunnyText);
    }

    @Override
    public void setClearSky() {

    }

    @Override
    public void setFoggy() {

    }

    @Override
    public void setCloudy() {

    }

    @Override
    public void setRainy() {

    }

    @Override
    public void setSnowy() {

    }

    @Override
    public void setThunder() {

    }

    @Override
    public void setDrizzle() {

    }

    private void updateWeather() {

    }

    private void updateWeatherTheme(@ColorRes int cardColorId,
                                    @ColorRes int textColorId) {
        int cardColor = ContextCompat.getColor(getContext(), cardColorId);
        int textColor = ContextCompat.getColor(getContext(), textColorId);

        weatherCard.setCardBackgroundColor(cardColor);

        weatherCity.setTextColor(textColor);
        weatherIcon.setTextColor(textColor);
        weatherType.setTextColor(textColor);
        weatherTemperature.setTextColor(textColor);
        weatherDelimiter.setBackgroundColor(textColor);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(R.string.main_activity_navigation_weather);
    }
}
