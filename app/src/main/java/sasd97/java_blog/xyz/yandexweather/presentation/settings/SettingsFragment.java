package sasd97.java_blog.xyz.yandexweather.presentation.settings;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;

/**
 * Created by alexander on 09/07/2017.
 */

public class SettingsFragment extends MvpAppCompatFragment
        implements SettingsView, SelectWeatherUpdateIntervalFragment.OnSelectItemListener {

    @BindColor(R.color.colorTextPrimary) int ordinaryTextColor;
    @BindColor(R.color.colorTextSecondary) int highlightTextColor;
    @BindColor(R.color.colorPrimaryDark) int highlightBackgroundColor;

    @BindView(R.id.fragment_settings_update_interval) TextView updateInterval;
    @BindView(R.id.fragment_settings_update_interval_switcher) SwitchCompat serviceSwitcher;

    @BindViews({R.id.fragment_settings_temperature_celsius, R.id.fragment_settings_temperature_fahrenheit})
    List<Button> temperatureButtonsGroup;

    @BindViews({R.id.fragment_settings_speed_ms, R.id.fragment_settings_speed_kmh})
    List<Button> speedButtonsGroup;

    @BindViews({R.id.fragment_settings_pressure_pascal, R.id.fragment_settings_pressure_mmhg})
    List<Button> pressureButtonsGroup;

    @InjectPresenter SettingsPresenter presenter;

    @ProvidePresenter
    public SettingsPresenter providePresenter() {
        return WeatherApp
                .get(getContext())
                .getMainComponent()
                .getSettingsPresenter();
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(R.string.main_activity_navigation_settings);
    }

    @Override
    public void highlightSettings() {
        enableButton(presenter.isCelsius() ? R.id.fragment_settings_temperature_celsius :
                R.id.fragment_settings_temperature_fahrenheit, temperatureButtonsGroup);

        enableButton(presenter.isMs() ? R.id.fragment_settings_speed_ms :
                R.id.fragment_settings_speed_kmh, speedButtonsGroup);

        enableButton(presenter.isMmHg() ? R.id.fragment_settings_pressure_mmhg :
                R.id.fragment_settings_pressure_pascal, pressureButtonsGroup);

        if (presenter.isServiceEnabled()) showSwitcherGroup();
        else hideSwitcherGroup();
    }

    @OnClick(R.id.fragment_settings_update_interval_switcher)
    public void onSwitchBackgroundServiceStateClick(View v) {
        presenter.switchBackgroundServiceState();
    }

    @OnClick(R.id.fragment_settings_update_interval)
    public void onChangeWeatherUpdateIntervalClick(View v) {
        new SelectWeatherUpdateIntervalFragment().show(getChildFragmentManager(), null);
    }

    @OnClick({R.id.fragment_settings_temperature_celsius, R.id.fragment_settings_temperature_fahrenheit})
    public void onTemperatureGroupClick(View v) {
        deselectGroup(temperatureButtonsGroup);
        enableButton(v.getId(), temperatureButtonsGroup);
        presenter.saveTemperature(v.getId());
    }

    @OnClick({R.id.fragment_settings_speed_ms, R.id.fragment_settings_speed_kmh})
    public void onSpeedGroupClick(View v) {
        deselectGroup(speedButtonsGroup);
        enableButton(v.getId(), speedButtonsGroup);
        presenter.saveSpeed(v.getId());
    }

    @OnClick({R.id.fragment_settings_pressure_pascal, R.id.fragment_settings_pressure_mmhg})
    public void onPressureGroupClick(View v) {
        deselectGroup(pressureButtonsGroup);
        enableButton(v.getId(), pressureButtonsGroup);
        presenter.savePressure(v.getId());
    }

    @Override
    public void showSwitcherGroup() {
        serviceSwitcher.setChecked(true);
        updateInterval.setVisibility(View.VISIBLE);
        updateInterval.setText(getString(R.string.settings_fragment_change_period,
                presenter.getCurrentInterval()));
    }

    @Override
    public void hideSwitcherGroup() {
        serviceSwitcher.setChecked(false);
        updateInterval.setVisibility(View.GONE);
    }

    @Override
    public void onIntervalSelected(int minutes) {
        updateInterval.setText(getString(R.string.settings_fragment_change_period,
                presenter.getCurrentInterval()));
    }

    private void deselectGroup(List<Button> group) {
        for (Button button: group) {
            button.setTextColor(ordinaryTextColor);
            button.getBackground().clearColorFilter();
        }
    }

    private void enableButton(int buttonId, List<Button> group) {
        for (Button button: group) {
            if (button.getId() != buttonId) continue;
            button.setTextColor(highlightTextColor);
            button.getBackground().setColorFilter(highlightBackgroundColor, PorterDuff.Mode.MULTIPLY);
        }
    }
}
