package sasd97.java_blog.xyz.yandexweather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by alexander on 09/07/2017.
 */

public class PushToBackStack implements FragmentCommand {

    @Override
    public void setContainer(@IdRes int containerId) {
    }

    @Override
    public FragmentTransaction apply(FragmentTransaction transaction) {
        return transaction.addToBackStack(null);
    }
}
