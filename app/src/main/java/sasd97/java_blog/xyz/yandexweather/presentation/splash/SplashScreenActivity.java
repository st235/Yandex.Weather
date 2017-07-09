package sasd97.java_blog.xyz.yandexweather.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;

import javax.inject.Inject;

import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainActivity;

/**
 * Created by alexander on 07/07/2017.
 */

public class SplashScreenActivity extends MvpAppCompatActivity {

    @Inject Router<Integer> router;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherApp.getAppComponent().inject(this);

        //router.pushForward(AppActivityRouter.WEATHER_ACTIVITY);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
