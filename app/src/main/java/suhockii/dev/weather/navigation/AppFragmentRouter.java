package suhockii.dev.weather.navigation;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import suhockii.dev.weather.navigation.fragments.FragmentCommand;

/**
 * Created by alexander on 07/07/2017.
 */

public class AppFragmentRouter implements Router<FragmentCommand> {

    private int fragmentContainerId;
    private FragmentManager fragmentManager;

    public AppFragmentRouter(@IdRes int fragmentContainerId,
                             @NonNull FragmentActivity activity) {
        this.fragmentContainerId = fragmentContainerId;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    @Override
    public void pushForward(FragmentCommand frame) {
        if (this.fragmentManager != null) {
            FragmentTransaction transaction = this.fragmentManager.beginTransaction();
            frame.setContainer(fragmentContainerId);
            frame.apply(transaction).commit();
        }
    }

    @Override
    public void pushForward(FragmentCommand frame, String tag) {
        if (this.fragmentManager != null) {
            FragmentTransaction transaction = this.fragmentManager.beginTransaction();
            frame.setContainer(fragmentContainerId);
            transaction.addToBackStack(tag);
            frame.apply(transaction, tag).commit();
        }
    }
}
