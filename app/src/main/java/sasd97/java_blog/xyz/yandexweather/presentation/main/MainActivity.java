package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.navigation.AppFragmentRouter;
import sasd97.java_blog.xyz.yandexweather.presentation.navigation.NavigationFragment;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment;
import sasd97.java_blog.xyz.yandexweather.utils.AndroidMath;
import sasd97.java_blog.xyz.yandexweather.utils.DrawerStateListener;
import sasd97.java_blog.xyz.yandexweather.utils.ElevationScrollListener;

public class MainActivity extends MvpAppCompatActivity implements MainView,
        SearchView.OnSuggestionListener, View.OnFocusChangeListener,
        ElevationScrollListener {

    private static final String CITY = "city";

    private Unbinder unbinder;
    private SimpleCursorAdapter cursorAdapter;
    private MenuItem miSearch;
    private SearchView searchView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation_layout) @Nullable RelativeLayout navigationLayout;
    @BindView(R.id.drawer_layout) @Nullable DrawerLayout drawer;
    @BindView(R.id.sliding_panel_layout) @Nullable SlidingPaneLayout slidingPaneLayout;

    public @InjectPresenter MainPresenter mainPresenter;
    private boolean isSearchOpenedFromClosedPanel;
    MenuItemCompat.OnActionExpandListener actionExpandListener;

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
        if (slidingPaneLayout != null) {
            initSlidingPanelListener();
            toolbar.setTranslationX(-AndroidMath.dp2px(320 - 80, getResources()));
        }
    }

    private void initSlidingPanelListener() {
        assert slidingPaneLayout != null;
        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //// TODO: 8/7/2017 move numbers to resources
                ((NavigationFragment) getSupportFragmentManager()
                        .findFragmentByTag(MainPresenter.TAG)).makeNavigationView(slideOffset);
                toolbar.setTranslationX(-AndroidMath.dp2px(320 - 80, getResources()) * (1 - slideOffset));
            }

            @Override
            public void onPanelOpened(View panel) {
            }

            @Override
            public void onPanelClosed(View panel) {
                if (toolbar.hasExpandedActionView()) toolbar.collapseActionView();
            }
        });
        actionExpandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (isSearchOpenedFromClosedPanel) {
                    slidingPaneLayout.closePane();
                    isSearchOpenedFromClosedPanel = false;
                }
                return true;
            }
        };
    }

    private void initNavigation(Bundle savedInstanceState) {
        mainPresenter.setNavigationRouter(new AppFragmentRouter(R.id.fragment_container_navigation, this));
        if (savedInstanceState == null) mainPresenter.openNavigationFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer != null) {
            assert getSupportActionBar() != null;
            if (drawer.isDrawerOpen(GravityCompat.START)) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            drawer.addDrawerListener(new DrawerStateListener() {
                @Override
                public void onDrawerStateChanged(int newState) {
                    if (toolbar.hasExpandedActionView()) toolbar.collapseActionView();
                }
            });
        }

    }

    private void initSearchSuggestsAdapter() {
        final String[] from = new String[]{CITY};
        final int[] to = new int[]{android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_search_suggest,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    private void initFragments(Bundle savedInstanceState) {
        mainPresenter.setWeatherRouter(new AppFragmentRouter(R.id.fragment_container_weather, this));
        if (savedInstanceState == null) mainPresenter.openWeatherFragment();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        mainPresenter.navigateWeatherTo(item.getItemId());
//        return true;
//    }

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
        }
        return super.onOptionsItemSelected(item);
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
        if (slidingPaneLayout != null)
            MenuItemCompat.setOnActionExpandListener(miSearch, actionExpandListener);
        searchView = (SearchView) MenuItemCompat.getActionView(miSearch);
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
        return true;
    }

    private void observeSearchInput(SearchView searchView) {
        RxSearchView.queryTextChanges(searchView)
                .doOnNext(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) showSuggestions(null);
                })
                .throttleLast(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) showSuggestions(null);
                    return !TextUtils.isEmpty(charSequence);
                })
                .map(CharSequence::toString)
                .flatMap(s -> mainPresenter.search(s))
                .subscribe();
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
    public void showSuggestions(String[] strings) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, CITY});
        if (strings != null) for (int i = 0; i < strings.length; i++) {
            c.addRow(new Object[]{i, strings[i]});
        }
        cursorAdapter.changeCursor(c);
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) toolbar.collapseActionView();
    }

    @Override
    public boolean onSuggestionClick(int position) {
        toolbar.collapseActionView();
        mainPresenter.saveCity(position);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNavigationLayoutElevation(int elevation) {
        assert navigationLayout != null;
        navigationLayout.animate().translationZ(elevation);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onToolbarElevation(int elevation) {
        toolbar.animate().translationZ(elevation);
    }
}
