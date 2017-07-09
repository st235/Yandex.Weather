package sasd97.java_blog.xyz.yandexweather.navigation.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by alexander on 09/07/2017.
 */

public class Replace extends FragmentCommandDecorator {

    private Fragment destinationFragment;

    public Replace(Fragment destinationFragment) {
        super();
        this.destinationFragment = destinationFragment;
    }

    @Override
    public FragmentTransaction onApply(FragmentTransaction transaction) {
        return transaction.replace(getContainer(), destinationFragment);
    }
}
