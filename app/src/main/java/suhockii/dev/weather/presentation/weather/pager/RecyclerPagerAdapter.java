package suhockii.dev.weather.presentation.weather.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import suhockii.dev.weather.R;

/**
 * Created by Maksim Sukhotski on 8/12/2017.
 */

public class RecyclerPagerAdapter extends FragmentPagerAdapter {
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