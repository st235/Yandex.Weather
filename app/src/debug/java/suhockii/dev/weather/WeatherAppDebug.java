package suhockii.dev.weather;

import com.facebook.stetho.Stetho;

/**
 * Created by alexander on 07/07/2017.
 */

public class WeatherAppDebug extends WeatherApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
