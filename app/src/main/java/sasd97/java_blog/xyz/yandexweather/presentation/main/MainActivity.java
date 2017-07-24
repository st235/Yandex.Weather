package sasd97.java_blog.xyz.yandexweather.presentation.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.WeatherApp;
import sasd97.java_blog.xyz.yandexweather.navigation.AppFragmentRouter;
import sasd97.java_blog.xyz.yandexweather.navigation.Router;
import sasd97.java_blog.xyz.yandexweather.navigation.fragments.FragmentCommand;

public class MainActivity extends MvpAppCompatActivity
        implements MainView,
        NavigationView.OnNavigationItemSelectedListener {

    private Unbinder unbinder;
    private Router<FragmentCommand> fragmentRouter = new AppFragmentRouter(R.id.fragment_container, this);

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    SearchView searchView;

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

        navigationView.setNavigationItemSelectedListener(this);
        mainPresenter.setRouter(fragmentRouter);

        if (savedInstanceState == null) onInit();
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mainPresenter.search(query);
        }
    }
}
