package suhockii.dev.weather.presentation.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import suhockii.dev.weather.R;
import suhockii.dev.weather.WeatherApp;
import suhockii.dev.weather.data.models.places.Place;
import suhockii.dev.weather.navigation.AppFragmentRouter;
import suhockii.dev.weather.presentation.drawer.DrawerFragment;
import suhockii.dev.weather.presentation.weather.WeatherFragment;
import suhockii.dev.weather.presentation.weather.WeatherView;
import suhockii.dev.weather.utils.AndroidUtils;
import suhockii.dev.weather.utils.ElevationScrollListener;
import suhockii.dev.weather.utils.GoogleListAdapter;
import suhockii.dev.weather.utils.NavigationFragmentAction;
import suhockii.dev.weather.utils.PlacesActions;

import static suhockii.dev.weather.presentation.drawer.DrawerFragment.TAG_NAVIGATION;
import static suhockii.dev.weather.presentation.weather.WeatherFragment.REQUEST_LOCATION;
import static suhockii.dev.weather.presentation.weather.WeatherFragment.TAG_WEATHER;

public class MainActivity extends MvpAppCompatActivity implements MainView,
        SearchView.OnSuggestionListener, View.OnFocusChangeListener,
        ElevationScrollListener, NavigationFragmentAction, DrawerLayout.DrawerListener {

    private static final String CITY = "city";
    public static final String TOOLBAR_VISIBILITY_KEY = "toolbar_visibility";

    private Unbinder unbinder;
    private SimpleCursorAdapter cursorAdapter;
    private MenuItem miSearch;
    private MenuItem miGps;
    private SearchView searchView;
    private boolean addToFavorites;
    private ActionBarDrawerToggle toggle;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation_layout) @Nullable RelativeLayout navigationLayout;
    @BindView(R.id.drawer_layout) @Nullable DrawerLayout drawer;
    @BindView(R.id.collapsing_toolbar) @Nullable CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.sliding_panel_layout) @Nullable SlidingPaneLayout slidingPaneLayout;
    @BindView(R.id.part_selected_toolbar_layout) RelativeLayout toolbarSelected;
    @BindView(R.id.part_selected_toolbar_delete) ImageView btnDelete;
    @BindView(R.id.part_selected_toolbar_back) ImageView btnBack;

    public @InjectPresenter MainPresenter mainPresenter;
    private boolean isSearchOpenedFromClosedPanel;
    MenuItem.OnActionExpandListener actionExpandListener;
    private boolean isToolbarSelectedVisible;
    private Disposable searchDisposable = Disposables.disposed();

    @ProvidePresenter
    public MainPresenter providePresenter() {
        return WeatherApp
                .get(this)
                .getMainComponent()
                .getMainPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        WeatherApp.get(this).getMainComponent().inject(this);

        setSupportActionBar(toolbar);

        initNavigation(savedInstanceState);
        initFragments(savedInstanceState);
        initSearchSuggestsAdapter();
        initSearchViewExpandListener();
        btnDelete.setOnClickListener(v -> {
            ((PlacesActions) getSupportFragmentManager()
                    .findFragmentByTag(TAG_NAVIGATION)).removeSelectedPlaces();
            onBackPressed();
        });
        btnBack.setOnClickListener(v -> onBackPressed());
        toolbarSelected.setVisibility(View.GONE);
        if (slidingPaneLayout != null) {
            initSlidingPanelListener();
            toolbar.setTranslationX(-AndroidUtils.dp2px(320 - 80, getResources()));
            toolbarSelected.setTranslationX(-AndroidUtils.dp2px(320 - 80, getResources()));
        }
    }

    private void initSearchViewExpandListener() {
        actionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                miGps.setVisible(false);
                notifyNavigationFragment(true);
                return true;
            }

            private void notifyNavigationFragment(boolean isExpand) {
                ((DrawerFragment) getSupportFragmentManager()
                        .findFragmentByTag(TAG_NAVIGATION)).onSearchViewExpand(isExpand);
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                notifyNavigationFragment(false);
                if (isSearchOpenedFromClosedPanel) {
                    assert slidingPaneLayout != null;
                    slidingPaneLayout.closePane();
                    isSearchOpenedFromClosedPanel = false;
                }
                miGps.setVisible(true);
                invalidateOptionsMenu();
                return true;
            }
        };
    }

    private void initSlidingPanelListener() {
        assert slidingPaneLayout != null;
        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                ((DrawerFragment) getSupportFragmentManager()
                        .findFragmentByTag(TAG_NAVIGATION)).makeNavigationView(slideOffset);
                int baseWidth = getResources().getDimensionPixelSize(R.dimen.drawer_minus_panel_width);
                toolbar.setTranslationX(-baseWidth * (1 - slideOffset));
                toolbarSelected.setTranslationX(-baseWidth * (1 - slideOffset));
            }

            @Override
            public void onPanelOpened(View panel) {
                ((PlacesActions) getSupportFragmentManager()
                        .findFragmentByTag(TAG_NAVIGATION)).setSlidingPanelOpen(true);
            }

            @Override
            public void onPanelClosed(View panel) {
                ((PlacesActions) getSupportFragmentManager()
                        .findFragmentByTag(TAG_NAVIGATION)).setSlidingPanelOpen(false);
                if (toolbar.hasExpandedActionView()) toolbar.collapseActionView();
            }
        });
    }

    private void initNavigation(Bundle savedInstanceState) {
        mainPresenter.setNavigationRouter(new AppFragmentRouter(R.id.fragment_container_navigation, this));
        if (savedInstanceState == null) mainPresenter.openNavigationFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isToolbarSelectedVisible) showToolbarSelected(true);
    }

    public void syncDrawer() {
        if (drawer != null) {
            assert getSupportActionBar() != null;
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (drawer.isDrawerOpen(GravityCompat.START))
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            drawer.addDrawerListener(this);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void unsyncDrawer() {
        if (drawer != null) {
            assert getSupportActionBar() != null;
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

            }
            drawer.removeDrawerListener(toggle);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initSearchSuggestsAdapter() {
        final String[] from = new String[]{CITY};
        final int[] to = new int[]{android.R.id.text1};
        cursorAdapter = new GoogleListAdapter(this, R.layout.item_search_suggest,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    private void initFragments(Bundle savedInstanceState) {
        mainPresenter.setWeatherRouter(new AppFragmentRouter(R.id.fragment_container_weather, this));
        if (savedInstanceState == null) mainPresenter.openWeatherFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
            else drawer.openDrawer(GravityCompat.START);
            return true;
        } else if (item.getTitle().equals("Search") && slidingPaneLayout != null &&
                !slidingPaneLayout.isOpen()) {
            slidingPaneLayout.openPane();
            isSearchOpenedFromClosedPanel = true;
        } else if (item.getItemId() == R.id.action_gps) {
            ((WeatherView) getSupportFragmentManager()
                    .findFragmentByTag(TAG_WEATHER)).updateWeatherByGps();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNewFavoritePlace(Place place) {
        ((PlacesActions) getSupportFragmentManager().findFragmentByTag(TAG_NAVIGATION))
                .onPlaceAdded(place);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            Fragment weatherFragment = getSupportFragmentManager().findFragmentByTag(TAG_WEATHER);
            if (weatherFragment != null) {
                weatherFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void closeDrawer() {
        if (drawer == null) return;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (isToolbarSelectedVisible) {
            cancelPlacesSelection();
            return;
        }
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
            return;
        }

        mainPresenter.onBackClicked();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        miSearch = menu.findItem(R.id.action_search);
        miGps = menu.findItem(R.id.action_gps);
        miSearch.setOnActionExpandListener(actionExpandListener);
        searchView = (SearchView) miSearch.getActionView();
        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate)
                .setBackgroundColor(Color.TRANSPARENT);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE))
                .getSearchableInfo(new ComponentName(this, MainActivity.class)));
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnSearchClickListener(v -> showSuggestions(null));
        searchView.setOnSuggestionListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);
        observeSearchInput(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void observeSearchInput(SearchView searchView) {
        RxSearchView.queryTextChanges(searchView)
                .doOnNext(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        showSuggestions(null);
                    }
                })
                .throttleLast(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .debounce(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        showSuggestions(null);
                    }
                    return !TextUtils.isEmpty(charSequence);
                })
                .map(CharSequence::toString)
                .flatMapCompletable(query -> mainPresenter.search(query))
                .doOnSubscribe(disposable -> {
                    searchDisposable.dispose();
                    searchDisposable = disposable;
                })
                .subscribe(() -> {/*ignore*/}, Throwable::printStackTrace);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
        }
    }

    @Override
    public void showSuggestions(String[] suggests) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, CITY});
        if (suggests != null) {
            for (int i = 0; i < (suggests.length > 4 ? 4 : suggests.length); i++) {
                c.addRow(new Object[]{i, suggests[i]});
            }
        }
        cursorAdapter.changeCursor(c);
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateWeatherContent() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_WEATHER);
        if (fragment == null) {
            return;
        }
        ((WeatherView) fragment).updateContent();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            toolbar.collapseActionView();
        }
    }

    @Override
    public boolean onSuggestionClick(int position) {
        toolbar.collapseActionView();
        mainPresenter.updateCurrentPlace(position, addToFavorites);
        if (!addToFavorites && drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        }
        addToFavorites = false;
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        changeSearchIconVisibility(fragment);
    }

    public void changeSearchIconVisibility(Fragment fragment) {
        if (miSearch == null) return;
        if (fragment instanceof WeatherFragment) miSearch.setVisible(true);
        else miSearch.setVisible(false);
    }

    @Override
    public void onNavigationLayoutElevation(int elevation) {
        assert navigationLayout != null;
        ViewCompat.animate(navigationLayout).translationZ(elevation);
    }

    @Override
    public void onToolbarElevation(int elevation) {
        ViewCompat.animate(toolbar).translationZ(elevation);
    }

    @Override
    public void onPlaceAdd() {
        if (searchView.isIconified()) {
            addToFavorites = true;
            miSearch.expandActionView();
            if (slidingPaneLayout != null) {
                isSearchOpenedFromClosedPanel = true;
                slidingPaneLayout.openPane();
            }
        }
    }

    @Override
    public void onPlaceSelect(int size) {
        if (size <= 0 || toolbarSelected.getVisibility() != View.VISIBLE) {
            showToolbarSelected(size != 0);
        }
    }

    @Override
    public void onPlaceClick(Place place, Place toReplace) {
        closeDrawer();
        mainPresenter.saveCurrentPlace(place, toReplace);
    }

    private void cancelPlacesSelection() {
        ((PlacesActions) getSupportFragmentManager()
                .findFragmentByTag(TAG_NAVIGATION)).cancelSelection();
        showToolbarSelected(false);
    }

    private void showToolbarSelected(boolean isToolbarSelectedVisible) {
        this.isToolbarSelectedVisible = isToolbarSelectedVisible;
        ViewCompat.animate(toolbar).alpha(isToolbarSelectedVisible ? 0 : 1)
                .withStartAction(() -> {
                    if (!isToolbarSelectedVisible) toolbar.setVisibility(View.VISIBLE);
                })
                .withEndAction(() -> {
                    if (isToolbarSelectedVisible) toolbar.setVisibility(View.INVISIBLE);
                });
        ViewCompat.animate(toolbarSelected)
                .alpha(isToolbarSelectedVisible ? 1 : 0)
                .withStartAction(() -> toolbarSelected.setVisibility(
                        isToolbarSelectedVisible ? View.VISIBLE : View.GONE));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(TOOLBAR_VISIBILITY_KEY, isToolbarSelectedVisible);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isToolbarSelectedVisible = savedInstanceState.getBoolean(TOOLBAR_VISIBILITY_KEY);
    }

    public boolean isSlidingPanelOpen() {
        return slidingPaneLayout == null || slidingPaneLayout.isOpen();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (isToolbarSelectedVisible) cancelPlacesSelection();
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (toolbar.hasExpandedActionView()) toolbar.collapseActionView();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        int alpha = (int) ((1 - slideOffset) * 255);
        miSearch.getIcon().setAlpha(alpha);
        miGps.getIcon().setAlpha(alpha);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }
}
