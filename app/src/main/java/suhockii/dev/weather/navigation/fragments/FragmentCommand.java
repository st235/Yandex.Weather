package suhockii.dev.weather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;

import suhockii.dev.weather.navigation.Command;

/**
 * Created by alexander on 09/07/2017.
 */

public interface FragmentCommand extends Command {
    void setContainer(@IdRes int containerId);
    FragmentTransaction apply(FragmentTransaction transaction);
    FragmentTransaction apply(FragmentTransaction transaction, String tag);
}
