package suhockii.dev.weather.utils;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public interface ElevationScrollListener {
    /**
     * This method should be called when tablet mode and scrolled items go under Navigation Layout
     */
    void onNavigationLayoutElevation(int elevation);

    /**
     * This method should be called when scrolled items go under Toolbar (ActionBar)
     */
    void onToolbarElevation(int elevation);
}
