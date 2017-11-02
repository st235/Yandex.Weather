package suhockii.dev.weather.navigation.fragments;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by alexander on 11/07/2017.
 */

public class FadeAnimation extends FragmentCommandDecorator {

    @Override
    protected FragmentTransaction onApply(FragmentTransaction transaction) {
        return transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    }

    @Override
    protected FragmentTransaction onApply(FragmentTransaction transaction, String tag) {
        return null;
    }
}
