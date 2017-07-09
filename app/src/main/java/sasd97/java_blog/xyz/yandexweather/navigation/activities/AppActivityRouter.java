package sasd97.java_blog.xyz.yandexweather.navigation.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.lang.ref.WeakReference;

import sasd97.java_blog.xyz.yandexweather.navigation.Router;


/**
 * Created by alexander on 09/07/2017.
 */

public class AppActivityRouter implements Router<ActivityCommand> {

    @Override
    public void pushForward(ActivityCommand frame) {
        if (!(frame instanceof MoveForward)) return;
        frame.apply();
    }
}
