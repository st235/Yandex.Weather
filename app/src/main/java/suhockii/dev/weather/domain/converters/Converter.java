package suhockii.dev.weather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public interface Converter<M, V> {
    boolean isApplicable(M mode);

    V convert(V value);
}
