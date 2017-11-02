package suhockii.dev.weather.presentation.weather;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import suhockii.dev.weather.R;
import suhockii.dev.weather.WeatherApp;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.presentation.main.MainActivity;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import suhockii.dev.weather.utils.AndroidUtils;
import suhockii.dev.weather.utils.Settings;

/**
 * Created by alexander on 09/07/2017.
 */

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView,
        AppBarLayout.OnOffsetChangedListener {

    public static final String TAG_WEATHER = "TAG_WEATHER";
    private SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @BindView(R.id.fragment_weather_icon) TextView weatherIcon;
    @BindView(R.id.fragment_weather_type) TextView weatherType;
    @BindView(R.id.fragment_weather_card) CardView weatherCard;
    @BindView(R.id.fragment_weather_humidity) TextView weatherHumidity;
    @BindView(R.id.fragment_weather_wind_speed) TextView weatherWindSpeed;
    @BindView(R.id.fragment_weather_pressure) TextView weatherPressure;
    @BindView(R.id.fragment_weather_temperature) TextView weatherTemperature;
    @BindView(R.id.fragment_weather_last_refresh) TextView weatherLastRefresh;
    @BindView(R.id.fragment_weather_min) TextView weatherMin;
    @BindView(R.id.fragment_weather_max) TextView weatherMax;
    @BindView(R.id.fragment_weather_vertical_delimiter) View weatherVerticalDelimiter;
    @BindView(R.id.fragment_weather_swipe_to_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_weather_view_group) ViewGroup viewGroup;
    @BindView(R.id.fragment_weather_temperature_extreme) TextView weatherTemperatureExtreme;
    @BindView(R.id.fragment_weather_recycler_forecast) RecyclerView forecastRecycler;
    @BindView(R.id.fragment_weather_appbarlayout) @Nullable AppBarLayout appBarLayout;
    @BindView(R.id.fragment_weather_gps_animation) LottieAnimationView gpsAnimationView;
    @BindBool(R.bool.is_tablet_horizontal) boolean isTabletHorizontal;
    @BindBool(R.bool.is_horizontal) boolean isHorizontal;

    @InjectPresenter WeatherPresenter presenter;

    private ForecastRecyclerAdapter forecastRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private boolean appBarIsExpanded = true;
    public static final int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    private Set<Runnable> runnables;
    private boolean isGpsDialogShown;
    private boolean canScrollRecycler = true;
    private Disposable animDisposable = Disposables.disposed();
    private boolean forecastIsLoaded;
    private WeatherModel currentWeather;
    private WeatherType currentWType;

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
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.fetchWeather();
            presenter.getForecast(null, true);
        });

        int orientation = (isTabletHorizontal) ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL;
        layoutManager = new LinearLayoutManager(getActivity(), orientation, false) {
            @Override
            public boolean canScrollVertically() {
                return canScrollRecycler;
            }
        };
        if (forecastRecycler != null) {
            forecastRecycler.setLayoutManager(layoutManager);
            forecastRecycler.setHasFixedSize(true);
        }
        weatherCard.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showWeather(@NonNull WeatherModel weather, @NonNull WeatherType type) {
        if (forecastIsLoaded) {
            updateWeatherTheme(type.getCardColor(), type.getTextColor());
            updateWeather(weather, type.getIconRes(), type.getTitleRes());
            weatherCard.setVisibility(View.VISIBLE);
            currentWeather = null;
            currentWeather = null;
        } else {
            currentWeather = weather;
            currentWType = type;
        }
        appBarLayout.setExpanded(true);
    }

    @Override
    public void showForecast(Pair<Map<WeatherModel, WeatherType[]>, Settings> pair) {
        forecastIsLoaded = true;
        forecastRecycler.setVisibility(View.INVISIBLE);

        if (currentWeather != null) {
            weatherCard.setVisibility(View.INVISIBLE);
            updateWeatherTheme(currentWType.getCardColor(), currentWType.getTextColor());
            updateWeather(currentWeather, currentWType.getIconRes(), currentWType.getTitleRes());
        }
        TransitionManager.beginDelayedTransition(viewGroup, new TransitionSet()
                .addTransition(new Explode())
                .addTransition(new Fade()));

        assert forecastRecycler != null;
        forecastRecyclerAdapter = new ForecastRecyclerAdapter(pair.first, pair.second)
                .setOnDisableScrollListener(disableScroll -> {
                    this.canScrollRecycler = disableScroll;
                    if (!disableScroll) {
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    } else {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
        if (currentWeather != null) {
            weatherCard.setVisibility(View.VISIBLE);
        }
        gpsAnimationView.setVisibility(View.INVISIBLE);
        forecastRecyclerAdapter.setHasStableIds(true);
        forecastRecycler.setAdapter(forecastRecyclerAdapter);
        forecastRecycler.scrollTo(0, 0);
        forecastRecycler.setVisibility(View.VISIBLE);
        gpsAnimationView.cancelAnimation();
    }

    @Override
    public void stopRefreshing() {
        if (!swipeRefreshLayout.isRefreshing()) return;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateContent() {
        presenter.updateContent();
    }

    @Override
    public void requestEnablingGps(Runnable callingMethod) {
        if (AndroidUtils.isLocationPermissionsDenied(getContext())) {
            new RxPermissions(getActivity())
                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (!granted) {
                            Toast.makeText(getContext(), R.string.on_gps_need_off, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        callingMethod.run();
                    });
            return;
        }
        if (runnables == null) {
            runnables = new LinkedHashSet<>();
        }
        runnables.add(callingMethod);
        if (!isGpsDialogShown) {
            showGpsSettingsDialog();
            isGpsDialogShown = true;
        }
    }

    @Override
    public void showGpsSearch() {
        TransitionManager.beginDelayedTransition(viewGroup, new Fade());
        gpsAnimationView.setVisibility(View.VISIBLE);
        weatherCard.setVisibility(View.INVISIBLE);
        forecastRecycler.setVisibility(View.INVISIBLE);
        gpsAnimationView.playAnimation();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.main_activity_action_search_place);
    }

    @Override
    public void showPlaceName(String placeName) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(placeName);
    }

    @Override
    public void updateWeatherByGps() {
        presenter.updateByGps();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideWeatherAndForecast() {
        forecastIsLoaded = false;
    }

    private void showGpsSettingsDialog() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {/*Ignore the connection.*/ }

                    @Override
                    public void onConnectionSuspended(int i) {
                        googleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(connectionResult ->
                        Log.d("Location error", "Location error " + connectionResult.getErrorCode())).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000L);
        locationRequest.setFastestInterval(5 * 1000L);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                try {
                    status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException e) {/*Ignore the error.*/ }
            } else {
                for (Runnable runnable : runnables) {
                    runnable.run();
                }
                runnables.clear();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && runnables != null) {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
            runnables.clear();
            isGpsDialogShown = false;
        }
    }

    private void updateWeather(@NonNull WeatherModel weather,
                               @StringRes int weatherIconId,
                               @StringRes int weatherTypeId) {
        weatherTemperature.setText(getString(R.string.weather_fragment_current_temperature,
                weather.getTemperature(), obtainTemperatureTitle()));
        weatherTemperatureExtreme.setText(getString(R.string.weather_fragment_current_temperature_extreme,
                weather.getMaxTemperature(), weather.getMinTemperature() - 1, obtainTemperatureTitle()));
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
        weatherMin.setTextColor(textColor);
        weatherMax.setTextColor(textColor);
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
        String formatted = fmt.format(new Date(updateTime));
        return getString(R.string.today_card) + formatted;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appBarLayout != null) appBarLayout.addOnOffsetChangedListener(this);
        ((MainActivity) getActivity()).changeSearchIconVisibility(this);
        appBarLayout.setExpanded(true);

        ((MainActivity) getActivity()).syncDrawer();
        layoutManager.scrollToPositionWithOffset(0,0);
    }

    /*For correct work of swipeRefreshLayout in conjunction with appBarLayout*/
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        swipeRefreshLayout.setEnabled(verticalOffset == 0);
        appBarIsExpanded = verticalOffset == 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        animDisposable.dispose();
        if (appBarLayout != null) appBarLayout.removeOnOffsetChangedListener(this);
        currentWeather = null;
        currentWType = null;
    }
}
