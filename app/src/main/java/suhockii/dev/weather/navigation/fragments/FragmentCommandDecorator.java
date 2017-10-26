package suhockii.dev.weather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by alexander on 09/07/2017.
 */

public abstract class FragmentCommandDecorator implements FragmentCommand {

    private int containerId;
    private FragmentCommand wrappedCommand;

    @Override
    public void setContainer(@IdRes int containerId) {
        this.containerId = containerId;
    }

    public int getContainer() {
        return containerId;
    }

    public void setNext(FragmentCommand command) {
        this.wrappedCommand = command;
    }

    @Override
    public FragmentTransaction apply(FragmentTransaction transaction) {
        FragmentTransaction t = onApply(transaction);

        if (wrappedCommand != null) {
            wrappedCommand.setContainer(containerId);
            return wrappedCommand.apply(t);
        }

        return t;
    }

    @Override
    public FragmentTransaction apply(FragmentTransaction transaction, String tag) {
        FragmentTransaction t = onApply(transaction, tag);

        if (wrappedCommand != null) {
            wrappedCommand.setContainer(containerId);
            return wrappedCommand.apply(t);
        }

        return t;
    }

    protected abstract FragmentTransaction onApply(FragmentTransaction transaction);

    protected abstract FragmentTransaction onApply(FragmentTransaction transaction, String tag);
}
