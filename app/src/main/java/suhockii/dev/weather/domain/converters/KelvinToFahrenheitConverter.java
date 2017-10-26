package suhockii.dev.weather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public class KelvinToFahrenheitConverter implements Converter<Integer, Float> {

    //1.8 x (K - 273) + 32

    @Override
    public boolean isApplicable(Integer mode) {
        return mode == ConvertersConfig.TEMPERATURE_FAHRENHEIT;
    }

    @Override
    public Float convert(Float input) {
        return 1.8f * (input - 273.15f) + 32.0f;
    }
}
