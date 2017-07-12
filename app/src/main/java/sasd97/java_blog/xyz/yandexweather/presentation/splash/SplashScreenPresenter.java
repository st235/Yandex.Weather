package sasd97.java_blog.xyz.yandexweather.presentation.splash;

import android.os.StrictMode;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;

import sasd97.java_blog.xyz.yandexweather.BuildConfig;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.ActivityCommand;

/**
 * Created by alexander on 09/07/2017.
 */

public class SplashScreenPresenter extends MvpPresenter<SplashScreenView> {

    private static final String TAG = SplashScreenPresenter.class.getCanonicalName();

    private Router<ActivityCommand> router;

    public SplashScreenPresenter(Router<ActivityCommand> router) {
        this.router = router;
    }

    public void onLoad(ActivityCommand command) {
        router.pushForward(command);
    }

    public void onDebug() {
        if (!BuildConfig.DEBUG) return;
        Log.i(TAG, "Application is in debug mode");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyDeath()
                .build());
    }
}
