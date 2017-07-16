package sasd97.java_blog.xyz.yandexweather.domain.converters;

/**
 * Created by alexander on 15/07/2017.
 */

public interface Converter<M, V> {
    boolean isApplicable(M mode);

    V convert(V value);
}
