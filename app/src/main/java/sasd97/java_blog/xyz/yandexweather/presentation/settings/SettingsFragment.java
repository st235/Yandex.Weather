package sasd97.java_blog.xyz.yandexweather.presentation.settings;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class SettingsFragment extends MvpAppCompatFragment implements SettingsView {

    @BindViews({R.id.fragment_settings_temperature_celsius,
            R.id.fragment_settings_temperature_fahrenheit})
    List<Button> temperatureButtons;

    @BindViews({R.id.fragment_settings_speed_ms,
            R.id.fragment_settings_speed_kmh})
    List<Button> speedButtons;

    @BindViews({R.id.fragment_settings_pressure_pascal,
            R.id.fragment_settings_pressure_mmhg})
    List<Button> pressureButtons;

    @BindColor(R.color.colorTextPrimary) int textColor;
    @BindColor(R.color.colorTextSecondary) int highlightTextColor;
    @BindColor(R.color.colorPrimaryDark) int highlightBgColor;

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
        if (presenter.isCelsius()) enableButton(R.id.fragment_settings_temperature_celsius, temperatureButtons);
        else enableButton(R.id.fragment_settings_temperature_fahrenheit, temperatureButtons);

        if (presenter.isMs()) enableButton(R.id.fragment_settings_speed_ms, speedButtons);
        else enableButton(R.id.fragment_settings_speed_kmh, speedButtons);

        if (presenter.isMmHg()) enableButton(R.id.fragment_settings_pressure_mmhg, pressureButtons);
        else enableButton(R.id.fragment_settings_pressure_pascal, pressureButtons);
    }

    @OnClick({R.id.fragment_settings_temperature_celsius,
            R.id.fragment_settings_temperature_fahrenheit})
    public void onTemperatureClick(View v) {
        deselectGroup(temperatureButtons);
        enableButton(v.getId(), temperatureButtons);
        presenter.saveTemperature(v.getId());
    }

    @OnClick({R.id.fragment_settings_speed_ms,
            R.id.fragment_settings_speed_kmh})
    public void onSpeedClick(View v) {
        deselectGroup(speedButtons);
        enableButton(v.getId(), speedButtons);
        presenter.saveSpeed(v.getId());
    }

    @OnClick({R.id.fragment_settings_pressure_pascal,
            R.id.fragment_settings_pressure_mmhg})
    public void onPressureClick(View v) {
        deselectGroup(pressureButtons);
        enableButton(v.getId(), pressureButtons);
        presenter.savePressure(v.getId());
    }

    private void deselectGroup(List<Button> group) {
        for (Button button: group) {
            button.setTextColor(textColor);
            button.getBackground().clearColorFilter();
        }
    }

    private void enableButton(int buttonId, List<Button> group) {
        for (Button button: group) {
            if (button.getId() != buttonId) continue;
            button.setTextColor(highlightTextColor);
            button.getBackground().setColorFilter(highlightBgColor, PorterDuff.Mode.MULTIPLY);
        }
    }
}
