package sasd97.java_blog.xyz.yandexweather.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public class AndroidMath {
    public static int dp2px(int dp, Resources r) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
