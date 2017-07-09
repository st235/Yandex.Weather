package sasd97.java_blog.xyz.yandexweather.navigation.activities;

import android.app.Activity;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * Created by alexander on 09/07/2017.
 */

public class MoveForward implements ActivityCommandDecorator {

    private WeakReference<Activity> currentActivity;
    private Class<?> destinationActivity;

    private ActivityCommand wrapped;

    public MoveForward(Activity currentActivity, Class<?> destinationActivity) {
        this.currentActivity = new WeakReference<>(currentActivity);
        this.destinationActivity = destinationActivity;
    }

    @Override
    public void setNext(ActivityCommand wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void apply() {
        Activity activity = currentActivity.get();
        if (activity == null) return;
        activity.startActivity(obtainIntent(activity, destinationActivity));

        if (wrapped != null) wrapped.apply();
    }

    private Intent obtainIntent(Activity from, Class<?> to) {
        return new Intent(from, to);
    }
}
