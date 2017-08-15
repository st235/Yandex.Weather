package sasd97.java_blog.xyz.yandexweather.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public class AndroidMath {
    public static int dp2px(int dp, Resources r) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static <C> List<C> asList(SerializableSparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    public static int intToDigit(int i){
        String s = String.valueOf(i);
        return Character.getNumericValue(s.charAt(s.length()-1));
    }
}
