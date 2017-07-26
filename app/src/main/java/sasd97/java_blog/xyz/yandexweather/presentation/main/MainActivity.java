package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.FragmentCommand;
import sasd97.java_blog.xyz.yandexweather.presentation.weather.WeatherFragment;
import sasd97.java_blog.xyz.yandexweather.utils.DrawerStateListener;

public class MainActivity extends MvpAppCompatActivity
        implements MainView, NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnSuggestionListener, View.OnFocusChangeListener {

    public static final String CITY = "city";

    private Unbinder unbinder;
    private Router<FragmentCommand> fragmentRouter = new AppFragmentRouter(R.id.fragment_container, this);
    private SimpleCursorAdapter cursorAdapter;
    private MenuItem miSearch;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    @InjectPresenter MainPresenter mainPresenter;

    @ProvidePresenter
    public MainPresenter providePresenter() {
        return WeatherApp
                .get(this)
                .getMainComponent()
                .getMainPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        WeatherApp.get(this).getMainComponent().inject(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.addDrawerListener(new DrawerStateListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (toolbar.hasExpandedActionView()) toolbar.collapseActionView();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        mainPresenter.setRouter(fragmentRouter);

        if (savedInstanceState == null) onInit();

        final String[] from = new String[]{CITY};
        final int[] to = new int[]{android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_search_suggest,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    private void onInit() {
        mainPresenter.openWeatherFragment();
    }

    @Override
    public void selectNavigationItem(@IdRes int id) {
        navigationView.setCheckedItem(id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mainPresenter.navigateTo(item.getItemId());
        return true;
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);

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
                // to prevent making requests too fast (as user may type fast),
                // throttleLast will emit last item during 100ms from the time
                // first item is emitted
                .throttleLast(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                // debounce will emit item only 200ms after last item is emitted
                // (after user types in last character)
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
            toolbar.collapseActionView();
            String query = intent.getStringExtra(SearchManager.QUERY);
            // TODO: 7/26/2017 Handle unpredictable queries :)
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
}
