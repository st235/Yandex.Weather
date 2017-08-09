package sasd97.java_blog.xyz.yandexweather.utils;

import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public interface PlacesActions {
    void onPlaceAdded(Place place);
    void removeSelectedPlaces();
    void cancelSelection();
}
