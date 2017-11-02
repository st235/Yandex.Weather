package suhockii.dev.weather.utils;

import suhockii.dev.weather.data.models.places.Place;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public interface NavigationFragmentAction {
    void onPlaceAdd();
    void onPlaceSelect(int size);
    void onPlaceClick(Place place, Place toReplace);
    boolean isSlidingPanelOpen();
}
