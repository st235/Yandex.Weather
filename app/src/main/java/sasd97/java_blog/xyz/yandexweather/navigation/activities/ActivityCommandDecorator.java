package sasd97.java_blog.xyz.yandexweather.navigation.activities;

/**
 * Created by alexander on 09/07/2017.
 */

public interface ActivityCommandDecorator extends ActivityCommand {
    void setNext(ActivityCommand command);
}
