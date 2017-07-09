package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayDeque;
import java.util.Deque;


/**
 * Created by alexander on 09/07/2017.
 */

public class AppActivityRouter implements Router<Integer> {

    public static final int SPLASH_SCREEN_ACTIVITY = 0;
    public static final int WEATHER_ACTIVITY = 1;

    private Context context;

    private SparseArray<Class<?>> activitiesMap;
    private Deque<Integer> activitiesStack = new ArrayDeque<>();

    public AppActivityRouter(@NonNull Context context,
                             @NonNull SparseArray<Class<?>> activitiesMap) {
        this.context = context;
        this.activitiesMap = activitiesMap;
    }
    @Override
    public void pushForward(Integer frame) {
        Class<?> to = activitiesMap.get(frame);
        context.startActivity(obtainIntent(to));
    }

    @Override
    public void pushBack() {
        activitiesStack.pop();
        if (!activitiesStack.isEmpty())
            throw new IllegalStateException("Your trying to navigate from last activity");
        pushForward(activitiesStack.peek());
    }

    private Intent obtainIntent(Class<?> to) {
        return new Intent(context, to);
    }
}
