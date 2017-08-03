package sasd97.java_blog.xyz.yandexweather.presentation.cities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;

import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;

/**
 * Created by alexander on 09/07/2017.
 */

public class CitiesFragment extends MvpAppCompatFragment implements CitiesView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cities, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}
