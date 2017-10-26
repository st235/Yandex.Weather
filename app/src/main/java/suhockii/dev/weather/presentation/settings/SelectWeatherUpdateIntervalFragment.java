package suhockii.dev.weather.presentation.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindArray;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import suhockii.dev.weather.R;
import suhockii.dev.weather.WeatherApp;

/**
 * Created by alexander on 15/07/2017.
 */

public class SelectWeatherUpdateIntervalFragment extends MvpAppCompatDialogFragment implements SelectWeatherUpdateIntervalView {

    public interface OnSelectItemListener {
        void onIntervalSelected(int minutes);
    }

    private int selected;
    private Unbinder unbinder;
    private OnSelectItemListener listener;

    @InjectPresenter
    SelectWeatherUpdateIntervalPresenter presenter;

    @BindArray(R.array.settings_fragment_available_interval_values) int[] values;
    @BindArray(R.array.settings_fragment_available_interval_titles) String[] titles;

    @ProvidePresenter
    public SelectWeatherUpdateIntervalPresenter providePresenter() {
        return WeatherApp
                .get(getContext())
                .getMainComponent()
                .getSelectIntervalPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnSelectItemListener) getParentFragment();
        } catch (ClassCastException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this, getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        int currentUpdateInterval = presenter.getCurrentIntervalValue();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.settings_fragment_available_intervals_title)
                .setSingleChoiceItems(titles, findItemByValue(currentUpdateInterval), this::handleSelection)
                .setPositiveButton(R.string.settings_fragment_available_intervals_ok, null)
                .setNegativeButton(R.string.settings_fragment_available_intervals_cancel,  null);

        return builder.create();
    }

    private int findItemByValue(int value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) return i;
        }
        return 0;
    }

    private void handleSelection(DialogInterface dialog, int position) {
        selected = values[position];
        presenter.saveCurrentIntervalValue(selected);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onIntervalSelected(selected);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
