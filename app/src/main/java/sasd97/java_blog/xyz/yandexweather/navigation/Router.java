package sasd97.java_blog.xyz.yandexweather.navigation;

/**
 * Created by alexander on 07/07/2017.
 */

public interface Router<F> {
    void pushForward(F frame);

    /*Oh, Alexander`s Router so unpredictable*/
    void pushForward(F frame, String tag);
}
