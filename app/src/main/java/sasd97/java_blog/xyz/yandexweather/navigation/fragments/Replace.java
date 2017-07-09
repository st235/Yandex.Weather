package sasd97.java_blog.xyz.yandexweather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by alexander on 09/07/2017.
 */

public class Replace implements FragmentCommandDecorator {

    private FragmentCommand wrapped;
    private int currentContainer;
    private Fragment destinationFragment;

    public Replace(Fragment destinationFragment) {
        this.destinationFragment = destinationFragment;
    }

    @Override
    public void setContainer(@IdRes int containerId) {
        this.currentContainer = containerId;
    }

    @Override
    public void setNext(FragmentCommand command) {
        this.wrapped = command;
    }

    @Override
    public FragmentTransaction apply(FragmentTransaction transaction) {
        FragmentTransaction t = transaction.replace(currentContainer, destinationFragment);

        if (wrapped != null) {
            wrapped.setContainer(currentContainer);
            return wrapped.apply(t);
        }

        return t;
    }
}
