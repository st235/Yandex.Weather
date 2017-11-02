package suhockii.dev.weather.navigation.activities;

import android.app.Activity;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * Created by alexander on 09/07/2017.
 */

public abstract class ActivityCommandDecorator implements ActivityCommand {

    private ActivityCommand wrapped;
    private WeakReference<Activity> currentActivity;

    protected ActivityCommandDecorator(Activity currentActivity) {
        this.currentActivity = new WeakReference<>(currentActivity);
    }

    public void setNext(ActivityCommand wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void apply() {
        Activity activity = currentActivity.get();
        if (activity == null) return;
        onApply(activity);
        if (wrapped != null) wrapped.apply();
    }

    protected abstract void onApply(Activity activity);

    protected Intent obtainIntent(Activity from, Class<?> to) {
        return new Intent(from, to);
    }
}
