package suhockii.dev.weather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by alexander on 09/07/2017.
 */

public class AddToBackStack implements FragmentCommand {

    private String tag;

    public AddToBackStack() {
    }

    public AddToBackStack(@NonNull String tag) {
        this.tag = tag;
    }

    @Override
    public void setContainer(@IdRes int containerId) {
    }

    @Override
    public FragmentTransaction apply(FragmentTransaction transaction) {
        return transaction.addToBackStack(tag);
    }

    @Override
    public FragmentTransaction apply(FragmentTransaction transaction, String tag) {
        return null;
    }
}
