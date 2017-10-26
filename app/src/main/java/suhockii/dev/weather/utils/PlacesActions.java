package suhockii.dev.weather.utils;

import suhockii.dev.weather.data.models.places.Place;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public interface PlacesActions {
    void onPlaceAdded(Place place);
    void removeSelectedPlaces();
    void cancelSelection();
    void setSlidingPanelOpen(boolean isOpen);
}
