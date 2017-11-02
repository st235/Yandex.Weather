package suhockii.dev.weather.navigation.activities;

import suhockii.dev.weather.navigation.Command;

/**
 * Created by alexander on 09/07/2017.
 */

public interface ActivityCommand extends Command {
    void apply();
}
