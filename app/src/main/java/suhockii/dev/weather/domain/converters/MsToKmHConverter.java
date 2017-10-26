package suhockii.dev.weather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public class MsToKmHConverter implements Converter<Integer, Float> {

    private static final float SCALE_FACTOR = 3.6f;

    @Override
    public boolean isApplicable(Integer mode) {
        return mode == ConvertersConfig.SPEED_KMH;
    }

    @Override
    public Float convert(Float value) {
        return value * SCALE_FACTOR;
    }
}
