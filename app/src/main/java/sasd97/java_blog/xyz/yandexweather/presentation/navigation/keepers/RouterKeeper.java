package sasd97.java_blog.xyz.yandexweather.presentation.navigation.keepers;

import sasd97.java_blog.xyz.yandexweather.presentation.navigation.Router;

/**
 * Created by alexander on 09/07/2017.
 */

public interface RouterKeeper<T> {
    Router<T> getRouter();
    void setRouter(Router<T> router);
}
