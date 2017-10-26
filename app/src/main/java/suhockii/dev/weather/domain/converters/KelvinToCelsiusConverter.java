package suhockii.dev.weather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public class KelvinToCelsiusConverter implements Converter<Integer, Float> {

    private static final float OFFSET_FACTOR = 273.15f;

    @Override
    public boolean isApplicable(Integer mode) {
        return mode == ConvertersConfig.TEMPERATURE_CELSIUS;
    }

    @Override
    public Float convert(Float input) {
        return input - OFFSET_FACTOR;
    }
}
