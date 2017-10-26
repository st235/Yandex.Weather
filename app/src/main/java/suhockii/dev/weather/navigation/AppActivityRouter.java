package suhockii.dev.weather.navigation;

import suhockii.dev.weather.navigation.activities.ActivityCommand;
import suhockii.dev.weather.navigation.activities.MoveForward;

/**
 * Created by alexander on 09/07/2017.
 */

public class AppActivityRouter implements Router<ActivityCommand> {

    @Override
    public void pushForward(ActivityCommand frame) {
        if (!(frame instanceof MoveForward)) return;
        frame.apply();
    }

    @Override
    public void pushForward(ActivityCommand frame, String tag) {

    }
}
