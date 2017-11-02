package suhockii.dev.weather.data.location;


import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import java.util.List;

import io.reactivex.SingleEmitter;
import suhockii.dev.weather.data.models.places.LatLng;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationProvider implements LocationListener {
    public static final String GPS_DISABLED = "Gps disabled";
    private static final long MIN_TIME_BW_UPDATES = 7000;
    private static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    private final LocationManager locationManager;
    private SingleEmitter<LatLng> emitter;

    public LocationProvider(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public Location getLastKnownLocation(SingleEmitter<LatLng> emitter) {
        this.emitter = emitter;
        Location location = null;
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnabled) {
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {


            }

        }

        //TODO implement when user not want GPS enabling
        if (isGPSEnabled) {
            try {
                if (location == null) {
                    Timber.d("Requesting location updates");

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

            } catch (Exception e) {
                if (emitter != null) {
                    emitter.onError(new Resources.NotFoundException(GPS_DISABLED));
                }
            }
        } else if (emitter != null) {
            emitter.onError(new Resources.NotFoundException(GPS_DISABLED));
        }
        List<String> providers = locationManager.getProviders(true);
        log(providers);
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }
        if (location != null) {
            emitter.onSuccess(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        return location;
    }

    private void log(List<String> providers) {
        if (providers.isEmpty()) {
            Timber.d("no providers");
        } else {
            for (String s : providers) {
                Timber.d(s);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (emitter != null) {
            Timber.d("OnSuccess!!!");
            emitter.onSuccess(new LatLng(location.getLatitude(), location.getLongitude()));
            locationManager.removeUpdates(this);
        }
        emitter = null;
        Timber.d(location.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Timber.d(s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Timber.d(s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Timber.d(s);
    }
}
