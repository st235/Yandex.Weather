package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainActivity;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by alexander on 09/07/2017.
 */

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView, AppBarLayout.OnOffsetChangedListener {

    private SimpleDateFormat fmt = new SimpleDateFormat("E, MMM dd, HH:mm", Locale.getDefault());

    @BindView(R.id.fragment_weather_icon) TextView weatherIcon;
    @BindView(R.id.fragment_weather_type) TextView weatherType;
    @BindView(R.id.fragment_weather_card) CardView weatherCard;
    @BindView(R.id.fragment_weather_humidity) TextView weatherHumidity;
    @BindView(R.id.fragment_weather_wind_speed) TextView weatherWindSpeed;
    @BindView(R.id.fragment_weather_pressure) TextView weatherPressure;
    @BindView(R.id.fragment_weather_temperature) TextView weatherTemperature;
    @BindView(R.id.fragment_weather_last_refresh) TextView weatherLastRefresh;
    @BindView(R.id.fragment_weather_vertical_delimiter) View weatherVerticalDelimiter;
    @BindView(R.id.fragment_weather_swipe_to_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_weather_temperature_extreme) TextView weatherTemperatureExtreme;
    @BindView(R.id.fragment_weather_recycler_forecast) RecyclerView forecastRecycler;
    @BindView(R.id.fragment_weather_appbarlayout) AppBarLayout appBarLayout;

    private ForecastRecyclerAdapter forecastRecyclerAdapter;

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

        swipeRefreshLayout.setOnRefreshListener(presenter::fetchWeather);

        int orientation = getResources().getConfiguration().orientation == 1 ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        forecastRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), orientation, false));
        forecastRecyclerAdapter = new ForecastRecyclerAdapter();
        forecastRecycler.setAdapter(forecastRecyclerAdapter);
        forecastRecycler.setHasFixedSize(true);
    }

    @Override
    public void setWeather(@NonNull WeatherModel weather, @NonNull WeatherType type) {
        updateWeatherTheme(type.getCardColor(), type.getTextColor());
        updateWeather(weather, type.getIconRes(), type.getTitleRes());
    }

    @Override
    public void stopRefreshing() {
        if (!swipeRefreshLayout.isRefreshing()) return;
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateWeather(@NonNull WeatherModel weather,
                               @StringRes int weatherIconId,
                               @StringRes int weatherTypeId) {
        weatherTemperature.setText(getString(R.string.weather_fragment_current_temperature,
                weather.getTemperature(), obtainTemperatureTitle()));
        weatherTemperatureExtreme.setText(getString(R.string.weather_fragment_current_temperature_extreme,
                weather.getMaxTemperature(), weather.getMinTemperature(), obtainTemperatureTitle()));
        weatherPressure.setText(getString(R.string.weather_fragment_pressure,
                weather.getPressure(), obtainPressureTitle()));
        weatherWindSpeed.setText(getString(R.string.weather_fragment_wind_speed,
                weather.getWindSpeed(), obtainSpeedTitle()));
        weatherHumidity.setText(getString(R.string.weather_fragment_humidity,
                weather.getHumidity()));
        weatherLastRefresh.setText(obtainRefreshTime(weather.getUpdateTime()));
        weatherIcon.setText(weatherIconId);
        weatherType.setText(weatherTypeId);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(weather.getCity());
    }

    private void updateWeatherTheme(@ColorRes int cardColorId,
                                    @ColorRes int textColorId) {
        int cardColor = ContextCompat.getColor(getContext(), cardColorId);
        int textColor = ContextCompat.getColor(getContext(), textColorId);

        weatherCard.setCardBackgroundColor(cardColor);

        weatherIcon.setTextColor(textColor);
        weatherType.setTextColor(textColor);
        weatherHumidity.setTextColor(textColor);
        weatherWindSpeed.setTextColor(textColor);
        weatherPressure.setTextColor(textColor);
        weatherTemperature.setTextColor(textColor);
        weatherLastRefresh.setTextColor(textColor);
        weatherTemperatureExtreme.setTextColor(textColor);
        weatherVerticalDelimiter.setBackgroundColor(textColor);
    }

    private String obtainTemperatureTitle() {
        return getString(presenter.isCelsius() ? R.string.all_weather_celsius : R.string.all_weather_fahrenheit);
    }

    private String obtainSpeedTitle() {
        return getString(presenter.isMs() ? R.string.all_weather_ms : R.string.all_weather_kmh);
    }

    private String obtainPressureTitle() {
        return getString(presenter.isMmHg() ? R.string.all_weather_mmhg : R.string.all_weather_pascal);
    }

    private String obtainRefreshTime(long updateTime) {
        return fmt.format(new Date(updateTime));
    }

    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
        ((MainActivity) getActivity()).changeSearchIconVisibility(this);
    }

    /*Hardcode for correct work of swipeRefreshLayout in conjunction with appBarLayout*/
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}
