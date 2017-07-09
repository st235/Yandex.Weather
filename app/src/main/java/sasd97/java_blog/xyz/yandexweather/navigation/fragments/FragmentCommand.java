package sasd97.java_blog.xyz.yandexweather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;

import sasd97.java_blog.xyz.yandexweather.navigation.Command;

/**
 * Created by alexander on 09/07/2017.
 */

public interface FragmentCommand extends Command {
    void setContainer(@IdRes int containerId);
    FragmentTransaction apply(FragmentTransaction transaction);
}
