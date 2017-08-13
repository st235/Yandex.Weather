package sasd97.java_blog.xyz.yandexweather.utils;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public interface NavigationFragmentAction {
    void onPlaceAdd();
    void onPlaceSelect(int size);
    void onPlaceClick(Place place, Place toReplace);
    boolean isSlidingPanelOpen();
}
