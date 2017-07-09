package sasd97.java_blog.xyz.yandexweather.navigation.fragments;

import android.support.v4.app.FragmentTransaction;

import sasd97.java_blog.xyz.yandexweather.navigation.Command;

/**
 * Created by alexander on 09/07/2017.
 */

public interface FragmentCommandDecorator extends FragmentCommand {
    void setNext(FragmentCommand command);
}
