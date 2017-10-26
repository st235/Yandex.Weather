package suhockii.dev.weather.presentation.drawer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import suhockii.dev.weather.R;
import suhockii.dev.weather.WeatherApp;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.presentation.main.MainActivity;
import suhockii.dev.weather.presentation.main.MainPresenter;
import suhockii.dev.weather.utils.AndroidUtils;
import suhockii.dev.weather.utils.ElevationScrollListener;
import suhockii.dev.weather.utils.FlipLayout;
import suhockii.dev.weather.utils.NavigationFragmentAction;
import suhockii.dev.weather.utils.PlacesActions;
import suhockii.dev.weather.utils.SerializableSparseArray;

/**
 * Created by alexander on 09/07/2017.
 */

public class DrawerFragment extends MvpAppCompatFragment implements DrawerView,
        PlacesActions {

    public static final String TAG_NAVIGATION = "navigation";
    public static final String SPARSE_ARRAY_KEY = "SparseArray";

    @BindView(R.id.fragment_navigation_recycler_cities) RecyclerView placesRecycler;
    @BindView(R.id.fragment_navigation_nav_view) RelativeLayout navigationView;
    @BindView(R.id.fragment_navigation_settings_image) ImageView navSettingsImage;
    @BindView(R.id.fragment_navigation_settings) TextView navSettings;
    @BindView(R.id.fragment_navigation_about) ImageView navAbout;
    @BindBool(R.bool.is_tablet) boolean isTablet;
    @BindBool(R.bool.is_tablet_vertical) boolean isTabletVertical;

    private DrawerRecyclerAdapter drawerRecyclerAdapter;
    private RecyclerView.OnScrollListener onScrollListener;
    private LinearLayoutManager layoutManager;
    private int actionBarHeight;

    @InjectPresenter DrawerPresenter presenter;

    @ProvidePresenter
    public DrawerPresenter providePresenter() {
        return WeatherApp
                .get(getContext())
                .getMainComponent()
                .getNavigationPresenter();
    }

    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBarHeight();
        if (!isTabletVertical) initOnNotTabletVertical();
        layoutManager = new LinearLayoutManager(getActivity());
        placesRecycler.setLayoutManager(layoutManager);
        placesRecycler.setHasFixedSize(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) initOnScrollListener();
        initNavigation();
    }

    private void initNavigation() {
        MainPresenter presenter = ((MainActivity) getActivity()).mainPresenter;
        navAbout.setOnClickListener(v -> presenter.navigateWeatherTo(R.id.main_activity_navigation_about));
        navSettings.setOnClickListener(v -> presenter.navigateWeatherTo(R.id.main_activity_navigation_settings));
        navSettingsImage.setOnClickListener(v -> presenter.navigateWeatherTo(R.id.main_activity_navigation_settings));
    }

    private void initActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, getResources().getDisplayMetrics());
        }
    }

    private void initOnNotTabletVertical() {
        navigationView.setTranslationY(actionBarHeight);
    }

    public void makeNavigationView(float slideOffset) {
        navigationView.setTranslationY(actionBarHeight * slideOffset);
        navSettings.setAlpha(slideOffset * slideOffset);
        navSettingsImage.setAlpha(1 - slideOffset * 2);
    }

    private void initOnScrollListener() {
        int toolbarElevation = getResources().getDimensionPixelSize(R.dimen.toolbar_elevation);
        int toolbarElevationBase = getResources().getDimensionPixelSize(R.dimen.toolbar_elevation_base);
        int navViewElevation = getResources().getDimensionPixelSize(R.dimen.nav_view_elevation);
        int navViewElevationBase = getResources().getDimensionPixelSize(R.dimen.nav_view_elevation_base);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ((ElevationScrollListener) getActivity()).onToolbarElevation(toolbarElevation);
                    onNavigationViewElevation(navViewElevation);
                } else {
                    if (isTablet) {
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            ((ElevationScrollListener) getActivity()).onToolbarElevation(toolbarElevationBase);
                        } else {
                            ((ElevationScrollListener) getActivity()).onToolbarElevation(toolbarElevation);
                        }
                    }
                    if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                            drawerRecyclerAdapter.getItemCount() - 1) {
                        onNavigationViewElevation(navViewElevationBase);
                    }
                }
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };
        placesRecycler.addOnScrollListener(onScrollListener);
        onNavigationViewElevation(navViewElevationBase);
        ((ElevationScrollListener) getActivity()).onToolbarElevation(toolbarElevationBase);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (onScrollListener != null) placesRecycler.removeOnScrollListener(onScrollListener);
    }

    private void onNavigationViewElevation(int elevation) {
        ViewCompat.animate(navigationView).translationZ(elevation);
    }

    @Override
    public void showPlaces(List<Place> places) {
        boolean panelOpen = ((NavigationFragmentAction) getActivity()).isSlidingPanelOpen();
        if (drawerRecyclerAdapter == null) {
            drawerRecyclerAdapter = new DrawerRecyclerAdapter(places);
        } else {
            drawerRecyclerAdapter.setPlaces(places);
            drawerRecyclerAdapter.notifyDataSetChanged();
        }
        if (!isTabletVertical) {
            drawerRecyclerAdapter.setSlidingPanelOpen(true);
        } else {
            drawerRecyclerAdapter.setSlidingPanelOpen(!panelOpen);
        }
        drawerRecyclerAdapter.setOnAddPlaceListener(() ->
                ((NavigationFragmentAction) getActivity()).onPlaceAdd());
        drawerRecyclerAdapter.setOnPlaceSelectListener(size ->
                ((NavigationFragmentAction) getActivity()).onPlaceSelect(size));
        drawerRecyclerAdapter.setOnPlaceClickListener((selected, toReplace) ->
                ((NavigationFragmentAction) getActivity()).onPlaceClick(selected, toReplace));
        placesRecycler.setAdapter(drawerRecyclerAdapter);
    }

    @Override
    public void onPlaceAdded(Place place) {
        drawerRecyclerAdapter.insertPlace(place);
    }

    @Override
    public void removeSelectedPlaces() {
        presenter.removeSelectedPlaces(AndroidUtils.asList(drawerRecyclerAdapter.getSelectedPlaces()));
        drawerRecyclerAdapter.removeSelectedPlaces();
    }

    @Override
    public void cancelSelection() {
        drawerRecyclerAdapter.cancelSelection();
        for (int i = layoutManager.findFirstVisibleItemPosition();
             i < layoutManager.findLastVisibleItemPosition(); i++) {
            FlipLayout flipLayout = (FlipLayout) layoutManager.findViewByPosition(i)
                    .findViewById(R.id.item_place_flip_layout);
            if (flipLayout.isFlipped()) flipLayout.toggleDown();
        }
    }

    @Override
    public void setSlidingPanelOpen(boolean isOpen) {
        drawerRecyclerAdapter.setSlidingPanelOpen(isOpen);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (null != drawerRecyclerAdapter) {
            outState.putSerializable(SPARSE_ARRAY_KEY, drawerRecyclerAdapter.getSelectedPlaces());
        }
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Serializable hashMap = savedInstanceState.getSerializable(SPARSE_ARRAY_KEY);
            if (hashMap == null) return;
            if (drawerRecyclerAdapter == null) drawerRecyclerAdapter =
                    new DrawerRecyclerAdapter((SerializableSparseArray<Place>) hashMap);
            else drawerRecyclerAdapter.setSelectedPlaces((SerializableSparseArray<Place>) hashMap);
        }
    }

    public void onSearchViewExpand(boolean isExpand) {
        View view = layoutManager.findViewByPosition(drawerRecyclerAdapter.getItemCount() - 1);
        if (view == null) return;
        view.setVisibility(isExpand ? View.INVISIBLE : View.VISIBLE);
    }
}
