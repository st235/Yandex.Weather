package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

/**
 * Created by alexander on 07/07/2017.
 */

public interface Router<F> {
    void pushForward(F frame);
    void pushBack();
}
