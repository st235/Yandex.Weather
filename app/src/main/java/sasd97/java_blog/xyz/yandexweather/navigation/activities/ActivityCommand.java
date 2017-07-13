package sasd97.java_blog.xyz.yandexweather.navigation.activities;

import sasd97.java_blog.xyz.yandexweather.navigation.Command;

/**
 * Created by alexander on 09/07/2017.
 */

public interface ActivityCommand extends Command {
    void apply();
}
