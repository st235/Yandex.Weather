package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainActivity;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.pager.RecyclerFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.pager.RecyclerPagerAdapter;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.AndroidUtils;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;
import sasd97.java_blog.xyz.yandexweather.utils.ViewPagerAction;

import static sasd97.java_blog.xyz.yandexweather.presentation.weather.pager.RecyclerPagerAdapter.getTagFor;

/**
 * Created by alexander on 09/07/2017.
 */

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView,
        AppBarLayout.OnOffsetChangedListener, ViewPagerAction {

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
    @BindView(R.id.fragment_weather_vertical_delimiter) View weatherVerticalDelimiter;
    @BindView(R.id.fragment_weather_swipe_to_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_weather_temperature_extreme) TextView weatherTemperatureExtreme;
    @BindView(R.id.fragment_weather_recycler_forecast) @Nullable RecyclerView forecastRecycler;
    @BindView(R.id.fragment_weather_appbarlayout) @Nullable AppBarLayout appBarLayout;
    @BindView(R.id.fragment_weather_fab) @Nullable FloatingActionButton fab;
    @BindView(R.id.fragment_weather_view_pager) @Nullable ViewPager pager;
    @BindBool(R.bool.is_tablet_horizontal) boolean isTabletHorizontal;
    @BindBool(R.bool.is_horizontal) boolean isHorizontal;

    @InjectPresenter WeatherPresenter presenter;

    private ForecastRecyclerAdapter forecastRecyclerAdapter;
    private RecyclerView.OnScrollListener onScrollListener;
    private LinearLayoutManager layoutManager;
    private boolean appBarIsExpanded = true;
    private RecyclerPagerAdapter pagerAdapter;
    public static final int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    private Queue<Runnable> locationRunnables;

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
            presenter.fetchForecast();
        });

        int orientation = (isTabletHorizontal) ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL;
        layoutManager = new LinearLayoutManager(getActivity(), orientation, false);
        if (forecastRecycler != null) {
            forecastRecycler.setLayoutManager(layoutManager);
            forecastRecycler.setHasFixedSize(true);
        }

        if (fab != null && appBarLayout != null) fab.setOnClickListener(v -> {
            layoutManager.scrollToPositionWithOffset(0, 0);

            appBarLayout.setExpanded(!appBarIsExpanded);
            fab.setImageResource(appBarIsExpanded ? R.drawable.ic_action_up : R.drawable.ic_action_down);
        });

        if (!isHorizontal) onVerticalMode();
        if (isTabletHorizontal) {
            assert pager != null;
            pagerAdapter = new RecyclerPagerAdapter(getChildFragmentManager());
            pager.setAdapter(pagerAdapter);
        } else {
//            presenter.fetchForecast();
        }
    }

    private void onVerticalMode() {
        assert fab != null;
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (fab.isShown() && layoutManager.findFirstCompletelyVisibleItemPosition() != 0 &&
                        layoutManager.findLastCompletelyVisibleItemPosition() !=
                                forecastRecyclerAdapter.getItemCount() - 1) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 ||
                                layoutManager.findLastCompletelyVisibleItemPosition() ==
                                        forecastRecyclerAdapter.getItemCount() - 1)) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        forecastRecycler.addOnScrollListener(onScrollListener);
    }

    @Override
    public void showWeather(@NonNull WeatherModel weather, @NonNull WeatherType type) {
        updateWeatherTheme(type.getCardColor(), type.getTextColor());
        updateWeather(weather, type.getIconRes(), type.getTitleRes());
    }

    @Override
    public void stopRefreshing() {
        if (!swipeRefreshLayout.isRefreshing()) return;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showForecast(Pair<LinkedHashMap<WeatherModel, WeatherType[]>, Settings> pair) {
        if (isTabletHorizontal) {
            RecyclerFragment recyclerFragment1 = (RecyclerFragment) getChildFragmentManager().findFragmentByTag(getTagFor(0));
            RecyclerFragment recyclerFragment2 = (RecyclerFragment) getChildFragmentManager().findFragmentByTag(getTagFor(1));
            if (recyclerFragment1 == null || recyclerFragment2 == null) return;
            LinkedHashMap<WeatherModel, WeatherType[]> rv1items;
            LinkedHashMap<WeatherModel, WeatherType[]> rv2items;
            rv1items = new LinkedHashMap<>(5);
            Set<WeatherModel> weatherModelSet = pair.first.keySet();
            if (weatherModelSet.size() == 0) return;
            for (int i = 0; i < 5; i++) {
                WeatherModel key = (WeatherModel) weatherModelSet.toArray()[i];
                rv1items.put(key, pair.first.get(key));
            }

            rv2items = new LinkedHashMap<>(11);
            for (int i = 5; i < 16; i++) {
                WeatherModel key = (WeatherModel) pair.first.keySet().toArray()[i];
                rv2items.put(key, pair.first.get(key));
            }
            recyclerFragment1.show(rv1items, pair.second);
            recyclerFragment2.show(rv2items, pair.second);
        } else {
            assert forecastRecycler != null;
            forecastRecyclerAdapter = new ForecastRecyclerAdapter(pair.first, pair.second);
            forecastRecycler.setAdapter(forecastRecyclerAdapter);
        }
    }

    @Override
    public void requestEnablingGps(Runnable callingMethod) {
        if (AndroidUtils.isLocationPermissionsDenied(getContext())) {
            new RxPermissions(getActivity())
                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (!granted) {
                            //TODO implement action when permissions not granted
                            return;
                        }
                        callingMethod.run();
                    });
            return;
        }
        if (locationRunnables == null) {
            locationRunnables = new PriorityQueue<>();
        }
        locationRunnables.add(callingMethod);
        showGpsSettingsDialog();
    }

    private void showGpsSettingsDialog() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        // Ignore the connection.
                    }

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
                    // check the result in onActivityResult().
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
            } else {
                runAllRunnables();
            }
        });
    }

    private void runAllRunnables() {
        Runnable locationRunnable = locationRunnables.poll();
        while (locationRunnable != null) {
            locationRunnable.run();
            locationRunnable = locationRunnables.poll();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && locationRunnables != null) {
            runAllRunnables();
        }
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
        String formatted = fmt.format(new Date(updateTime));
        return getString(R.string.today_card) + formatted;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appBarLayout != null) appBarLayout.addOnOffsetChangedListener(this);
        ((MainActivity) getActivity()).changeSearchIconVisibility(this);
    }

    /*For correct work of swipeRefreshLayout in conjunction with appBarLayout*/
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        assert fab != null;
        swipeRefreshLayout.setEnabled(verticalOffset == 0);
        appBarIsExpanded = verticalOffset == 0;
        if (verticalOffset == 0) fab.show();
        fab.setImageResource(verticalOffset == 0 ? R.drawable.ic_action_down : R.drawable.ic_action_up);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (appBarLayout != null) appBarLayout.removeOnOffsetChangedListener(this);
        if (onScrollListener != null) forecastRecycler.removeOnScrollListener(onScrollListener);
    }

    @Override
    public void onPagerFragmentAttached() {
        presenter.fetchForecast();
    }

    @Override
    public void onNextFabClick() {
        assert pager != null;
        pager.setCurrentItem(1, true);
    }

    @Override
    public void onPrevFabClick() {
        assert pager != null;
        pager.setCurrentItem(0, true);
    }
}
