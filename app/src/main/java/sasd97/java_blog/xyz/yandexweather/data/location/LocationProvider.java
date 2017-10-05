package sasd97.java_blog.xyz.yandexweather.data.location;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.RequiresPermission;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationProvider {
    private final LocationManager locationManager;

    public LocationProvider(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            // noinspection MissingPermission
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
