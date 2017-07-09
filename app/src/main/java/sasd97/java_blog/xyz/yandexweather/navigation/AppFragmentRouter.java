package sasd97.java_blog.xyz.yandexweather.navigation;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by alexander on 07/07/2017.
 */

public class AppFragmentRouter implements Router<Fragment> {

    private int fragmentContainerId;
    private FragmentManager fragmentManager;

    public AppFragmentRouter(@IdRes int fragmentContainerId,
                             @NonNull FragmentActivity activity) {
        this.fragmentContainerId = fragmentContainerId;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    @Override
    public void pushForward(Fragment frame) {
        this.fragmentManager
                .beginTransaction()
                .replace(fragmentContainerId, frame)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void pushBack() {
        this.fragmentManager
                .popBackStack();
    }
}
