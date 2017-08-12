package sasd97.java_blog.xyz.yandexweather.presentation.weather.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedHashMap;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by Maksim Sukhotski on 8/12/2017.
 */

public class RecyclerPagerAdapter extends FragmentPagerAdapter {
    LinkedHashMap<WeatherModel, WeatherType[]> rv1items;
    LinkedHashMap<WeatherModel, WeatherType[]> rv2items;


    public RecyclerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public static String getTagFor(int position) {
        return "android:switcher:" + R.id.fragment_weather_view_pager + ":" + position;
    }

    @Override
    public Fragment getItem(int position) {
        return RecyclerFragment.newInstance(position == 1);
    }

    @Override
    public int getCount() {
        return 2;
    }
}