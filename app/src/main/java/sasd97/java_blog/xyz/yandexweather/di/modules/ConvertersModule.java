package sasd97.java_blog.xyz.yandexweather.di.modules;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import sasd97.java_blog.xyz.yandexweather.domain.converters.Converter;
import sasd97.java_blog.xyz.yandexweather.domain.converters.KelvinToCelsiusConverter;
import sasd97.java_blog.xyz.yandexweather.domain.converters.KelvinToFahrenheitConverter;
import sasd97.java_blog.xyz.yandexweather.domain.converters.MsToKmHConverter;
import sasd97.java_blog.xyz.yandexweather.domain.converters.PascalToMmHgConverter;

import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.PRESSURE_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.SPEED_CONVERTERS_KEY;
import static sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig.TEMPERATURE_CONVERTERS_KEY;

/**
 * Created by alexander on 15/07/2017.
 */

@Module
public class ConvertersModule {

    @Provides
    @IntoMap
    @StringKey(TEMPERATURE_CONVERTERS_KEY)
    public List<Converter<Integer, Float>> provideTemperatureConverters() {
        List<Converter<Integer, Float>> list = new ArrayList<>();
        list.add(new KelvinToCelsiusConverter());
        list.add(new KelvinToFahrenheitConverter());
        return list;
    }

    @Provides
    @IntoMap
    @StringKey(PRESSURE_CONVERTERS_KEY)
    public List<Converter<Integer, Float>> providePressureConverters() {
        List<Converter<Integer, Float>> list = new ArrayList<>();
        list.add(new PascalToMmHgConverter());
        return list;
    }

    @Provides
    @IntoMap
    @StringKey(SPEED_CONVERTERS_KEY)
    public List<Converter<Integer, Float>> provideSpeedConverters() {
        List<Converter<Integer, Float>> list = new ArrayList<>();
        list.add(new MsToKmHConverter());
        return list;
    }
}
