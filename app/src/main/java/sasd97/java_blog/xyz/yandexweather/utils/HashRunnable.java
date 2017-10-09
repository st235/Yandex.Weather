package sasd97.java_blog.xyz.yandexweather.utils;

import android.support.annotation.NonNull;

/**
 * Created by Maksim Sukhotski on 8/10/2017.
 */

public class HashRunnable implements Runnable {

    @NonNull
    private final String runnableName;
    private final Runnable runnable;


    public HashRunnable(Runnable runnable, @NonNull String tag) {
        this.runnable = runnable;
        this.runnableName = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashRunnable that = (HashRunnable) o;

        return runnableName.equals(that.runnableName);

    }

    @Override
    public int hashCode() {
        return runnableName.hashCode();
    }

    @Override
    public void run() {
        runnable.run();
    }

    public String getRunnableName() {
        return runnableName;
    }
}
