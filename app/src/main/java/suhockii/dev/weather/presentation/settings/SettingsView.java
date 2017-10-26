package suhockii.dev.weather.presentation.settings;

import com.arellomobile.mvp.MvpView;

/**
 * Created by alexander on 15/07/2017.
 */

public interface SettingsView extends MvpView {
    void highlightSettings();
    void showSwitcherGroup();
    void hideSwitcherGroup();
}
