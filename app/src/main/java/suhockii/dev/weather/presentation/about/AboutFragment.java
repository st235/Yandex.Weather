package suhockii.dev.weather.presentation.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;

import suhockii.dev.weather.R;
import suhockii.dev.weather.presentation.main.MainActivity;

/**
 * Created by alexander on 09/07/2017.
 */

public class AboutFragment extends MvpAppCompatFragment {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem miGps = menu.findItem(R.id.action_gps);

        search.setVisible(false);
        miGps.setVisible(false);
    }

    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).unsyncDrawer();
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(R.string.main_activity_navigation_about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
