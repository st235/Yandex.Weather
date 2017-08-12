package sasd97.java_blog.xyz.yandexweather.presentation.weather.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.ForecastRecyclerAdapter;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.PagerAction;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

import static sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment.TAG_WEATHER;

/**
 * Created by Maksim Sukhotski on 8/12/2017.
 */

public class RecyclerFragment extends Fragment {

    static final String PAGE_LAST_ARG = "arg_page_last";

    @BindView(R.id.part_recycler_forecast) RecyclerView forecastRecycler;
    @BindView(R.id.part_recycler_fab) FloatingActionButton fab;

    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    ForecastRecyclerAdapter forecastRecyclerAdapter;

    static RecyclerFragment newInstance(boolean isLastFragment) {
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(PAGE_LAST_ARG, isLastFragment);
        recyclerFragment.setArguments(arguments);
        return recyclerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId;
        if (getArguments().getBoolean(PAGE_LAST_ARG)) layoutId = R.layout.part_recycler_view_right;
        else layoutId = R.layout.part_recycler_view_left;
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab.hide();
        if (getArguments().getBoolean(PAGE_LAST_ARG)) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        }
        fab.setOnClickListener(v -> {
            if (getArguments().getBoolean(PAGE_LAST_ARG)) {
                gridLayoutManager.scrollToPositionWithOffset(0, 0);
                ((PagerAction) getFragmentManager().findFragmentByTag(TAG_WEATHER)).onPrevFabClick();
                fab.hide();
            } else {
                ((PagerAction) getFragmentManager().findFragmentByTag(TAG_WEATHER)).onNextFabClick();
            }
        });
        forecastRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (fab.isShown() && (layoutManager != null ? (layoutManager.findLastCompletelyVisibleItemPosition() !=
                        forecastRecyclerAdapter.getItemCount() - 1) :
                        (gridLayoutManager.findLastCompletelyVisibleItemPosition() !=
                                forecastRecyclerAdapter.getItemCount() - 1))) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager != null ? (layoutManager.findLastCompletelyVisibleItemPosition() ==
                                forecastRecyclerAdapter.getItemCount() - 1) :
                                (gridLayoutManager.findLastCompletelyVisibleItemPosition() ==
                                        forecastRecyclerAdapter.getItemCount() - 1))) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getBoolean(PAGE_LAST_ARG)) {
            ((PagerAction) getFragmentManager()
                    .findFragmentByTag(TAG_WEATHER)).onPagerFragmentAttached();
        }
    }

    public void show(LinkedHashMap<WeatherModel, WeatherType[]> items, Settings settings) {
        boolean isSecondary = getArguments().getBoolean(PAGE_LAST_ARG);
        forecastRecyclerAdapter = new ForecastRecyclerAdapter(items, settings, isSecondary);
        forecastRecycler.setAdapter(forecastRecyclerAdapter);
        if (isSecondary) {
            forecastRecycler.setLayoutManager(gridLayoutManager);
        } else {
            forecastRecycler.setLayoutManager(layoutManager);
        }
    }
}
