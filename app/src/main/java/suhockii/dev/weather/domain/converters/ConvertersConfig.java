package suhockii.dev.weather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public interface ConvertersConfig {
    String SPEED_CONVERTERS_KEY = "speed";
    String PRESSURE_CONVERTERS_KEY = "pressure";
    String TEMPERATURE_CONVERTERS_KEY = "tempearture";

    //temperature
    int TEMPERATURE_CELSIUS = 0;
    int TEMPERATURE_FAHRENHEIT = 1;

    //pressure
    int PRESSURE_MMHG = 0;
    int PRESSURE_PASCAL = 1;

    //speed
    int SPEED_MS = 0;
    int SPEED_KMH = 1;
}
