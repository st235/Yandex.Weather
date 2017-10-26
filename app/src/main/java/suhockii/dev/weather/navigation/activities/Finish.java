package suhockii.dev.weather.navigation.activities;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by alexander on 09/07/2017.
 */

public class Finish implements ActivityCommand {

    private WeakReference<Activity> currentActivity;

    public Finish(Activity currentActivity) {
        this.currentActivity = new WeakReference<>(currentActivity);
    }

    @Override
    public void apply() {
        Activity act = currentActivity.get();
        if (act == null) return;
        act.finish();
    }
}
