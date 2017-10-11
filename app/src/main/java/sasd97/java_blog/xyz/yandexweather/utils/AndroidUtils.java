package sasd97.java_blog.xyz.yandexweather.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Maksim Sukhotski on 8/5/2017.
 */

public class AndroidUtils {
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

    public static boolean isLocationPermissionsDenied(Context context) {
        int fineLocPerm = ContextCompat.checkSelfPermission(
                context, ACCESS_FINE_LOCATION);
        int coarseLocPerm = ContextCompat.checkSelfPermission(
                context, ACCESS_COARSE_LOCATION);

        return fineLocPerm == PackageManager.PERMISSION_DENIED ||
                coarseLocPerm == PackageManager.PERMISSION_DENIED;
    }

    public static int[] concatArrays(int[]... arrays) {
        int length = 0;
        for (int[] array : arrays) {
            length += array.length;
        }
        int[] result = new int[length];
        int pos = 0;
        for (int[] array : arrays) {
            for (int element : array) {
                result[pos] = element;
                pos++;
            }
        }
        return result;
    }
}
