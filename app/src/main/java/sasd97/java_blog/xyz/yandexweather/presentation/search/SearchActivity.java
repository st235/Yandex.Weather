package sasd97.java_blog.xyz.yandexweather.presentation.search;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.Unbinder;

public class SearchActivity extends MvpAppCompatActivity implements SearchView {

    private Unbinder unbinder;
    android.support.v7.widget.SearchView searchView;

    @InjectPresenter SearchPresenter searchPresenter;

}
