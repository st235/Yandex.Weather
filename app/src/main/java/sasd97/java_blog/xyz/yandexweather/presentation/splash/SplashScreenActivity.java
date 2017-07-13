package sasd97.java_blog.xyz.yandexweather.presentation.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.ActivityCommand;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.ActivityCommandDecorator;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.Finish;
import sasd97.java_blog.xyz.yandexweather.navigation.activities.MoveForward;
import sasd97.java_blog.xyz.yandexweather.presentation.main.MainActivity;

/**
 * Created by alexander on 07/07/2017.
 */

public class SplashScreenActivity extends MvpAppCompatActivity
    implements SplashScreenView {

    @InjectPresenter SplashScreenPresenter presenter;

    @ProvidePresenter
    public SplashScreenPresenter providePresenter() {
        return WeatherApp.get(this)
                .getSplashScreenComponent().getSplashScreenPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherApp.get(this).getSplashScreenComponent().inject(this);
        presenter.onLoad(loadNext());
    }

    private ActivityCommand loadNext() {
        ActivityCommandDecorator moveTo = new MoveForward(this, MainActivity.class);
        ActivityCommand finish = new Finish(this);
        moveTo.setNext(finish);
        return moveTo;
    }
}
