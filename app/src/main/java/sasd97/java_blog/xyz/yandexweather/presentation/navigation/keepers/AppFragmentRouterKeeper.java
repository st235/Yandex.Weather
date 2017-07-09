package sasd97.java_blog.xyz.yandexweather.presentation.navigation.keepers;

import android.support.v4.app.Fragment;

import sasd97.java_blog.xyz.yandexweather.presentation.navigation.Router;

/**
 * Created by alexander on 09/07/2017.
 */

public class AppFragmentRouterKeeper implements RouterKeeper<Fragment> {

    private Router<Fragment> router;

    @Override
    public Router<Fragment> getRouter() {
        return router;
    }

    @Override
    public void setRouter(Router<Fragment> router) {
        this.router = router;
    }
}
