package suhockii.dev.weather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public class PascalToMmHgConverter implements Converter<Integer, Float> {

    private static final float SCALE_FACTOR = 0.75f;

    @Override
    public boolean isApplicable(Integer mode) {
        return mode == ConvertersConfig.PRESSURE_MMHG;
    }

    @Override
    public Float convert(Float input) {
        return input * SCALE_FACTOR;
    }
}
