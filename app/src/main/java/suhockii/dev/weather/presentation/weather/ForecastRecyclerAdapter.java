package suhockii.dev.weather.presentation.weather;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import suhockii.dev.weather.R;
import suhockii.dev.weather.domain.converters.ConvertersConfig;
import suhockii.dev.weather.domain.models.WeatherModel;
import suhockii.dev.weather.presentation.weatherTypes.WeatherType;
import suhockii.dev.weather.utils.Settings;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.RecyclerViewHolder> {
    private static final int TYPE_FORECAST = 1;
    private static final int TYPE_LICENSE = 2;
    public static final int API_LICENCE_COUNT = 1;
    private final List<WeatherModel> weatherModels;
    private final List<WeatherType[]> weatherTypes;
    private final boolean[] expandedPositions = new boolean[5];
    private final Settings settings;

    private RecyclerView recyclerView;

    private OnDisableScrollListener onDisableScrollListener;

    interface OnDisableScrollListener {
        void onDisableScroll(boolean disableScroll);
    }

    ForecastRecyclerAdapter setOnDisableScrollListener(OnDisableScrollListener onDisableScrollListener) {
        this.onDisableScrollListener = onDisableScrollListener;
        return this;
    }

    ForecastRecyclerAdapter(Map<WeatherModel, WeatherType[]> forecasts,
                            Settings settings) {
        WeatherModel[] weatherModelArray = forecasts.keySet().toArray(new WeatherModel[0]);
        WeatherType[][] weatherTypesArray = forecasts.values().toArray(new WeatherType[0][]);
        this.weatherModels = new ArrayList<>(Arrays.asList(weatherModelArray));
        this.weatherTypes = new ArrayList<>(Arrays.asList(weatherTypesArray));
        this.settings = settings;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @LayoutRes int layoutRes = viewType == TYPE_LICENSE ? R.layout.item_api_license : R.layout.item_forecast;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);

        return new RecyclerViewHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_LICENSE : TYPE_FORECAST;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecyclerViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            return;
        }
        Resources resources = holder.itemView.getResources();
        setTheme(holder, position);
        setData(holder, position, resources);
        boolean isExpandable = position < 4;
        updateContentSize(holder, !(isExpandable && expandedPositions[position]), isExpandable);

        RxView.clicks(holder.itemView)
                .filter(o -> isExpandable)
                .throttleFirst(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(ignore -> {
                    boolean needCollapsing = holder.itemView
                            .findViewById(R.id.content_forecast_tv_morning)
                            .getVisibility() == View.VISIBLE;

                    TransitionManager.beginDelayedTransition(recyclerView, new TransitionSet()
                            .addTransition(new ChangeBounds())
                            .addTransition(new Fade().setDuration(needCollapsing ? 100 : 400))
                            .setDuration(200)
                            .addListener(new Transition.TransitionListenerAdapter() {
                                @Override
                                public void onTransitionStart(Transition transition) {
                                    onDisableScrollListener.onDisableScroll(false);
                                    super.onTransitionStart(transition);
                                }

                                @Override
                                public void onTransitionEnd(Transition transition) {
                                    super.onTransitionEnd(transition);
                                    onDisableScrollListener.onDisableScroll(true);
                                }
                            }));
                    updateContentSize(holder, needCollapsing, isExpandable);
                    expandedPositions[position] = !needCollapsing;
                }, Throwable::printStackTrace);
    }

    /**
     * Set icon morning because forecast16 response contains only 1 weather type for whole day,
     * and we put it in [0] position in array.
     */
    @SuppressWarnings("ConstantConditions")
    private void setData(RecyclerViewHolder holder, int position, Resources resources) {
        WeatherModel weather = weatherModels.get(position);

        holder.date.setText(position == 0 ? resources.getString(R.string.tomorrow) : weather.getReadableDate());
        holder.tempMain.setText(resources.getString(R.string.forecasts_temperature_iconyfied,
                weather.getDayTemperature(), obtainTemperatureTitle(resources, settings.getTemp())));
        holder.tempMorning.setText(resources.getString(R.string.forecasts_temperature,
                weather.getMorningTemperature(), obtainTemperatureTitle(resources, settings.getTemp())));
        holder.tempDay.setText(resources.getString(R.string.forecasts_temperature,
                weather.getDayTemperature(), obtainTemperatureTitle(resources, settings.getTemp())));
        holder.tempEvening.setText(resources.getString(R.string.forecasts_temperature,
                weather.getEveningTemperature(), obtainTemperatureTitle(resources, settings.getTemp())));
        holder.tempNight.setText(resources.getString(R.string.forecasts_temperature,
                weather.getNightTemperature(), obtainTemperatureTitle(resources, settings.getTemp())));
        holder.tempExtreme.setText(resources.getString(R.string.weather_fragment_current_temperature_extreme,
                weather.getMaxTemperature(), weather.getMinTemperature(), obtainTemperatureTitle(resources, settings.getTemp())));
        boolean isIconifiedForecast = weatherTypes.get(position).length == 5;
        int iconVisibility = isIconifiedForecast ? View.VISIBLE : View.GONE;
        holder.iconMorning.setVisibility(iconVisibility);
        holder.iconDay.setVisibility(iconVisibility);
        holder.iconEvening.setVisibility(iconVisibility);
        holder.iconNight.setVisibility(iconVisibility);
        if (isIconifiedForecast) {
            holder.iconMain.setText(weatherTypes.get(position)[0].getIconRes());
            holder.iconMorning.setText(weatherTypes.get(position)[1].getIconRes());
            holder.iconDay.setText(weatherTypes.get(position)[2].getIconRes());
            holder.iconEvening.setText(weatherTypes.get(position)[3].getIconRes());
            holder.iconNight.setText(weatherTypes.get(position)[4].getIconRes());
        }
        holder.iconMain.setText(weatherTypes.get(position)[0].getIconRes());
    }

    @SuppressWarnings("ConstantConditions")
    private void setTheme(RecyclerViewHolder holder, int position) {
        int cardColor = weatherTypes.get(position)[0].getCardColor();
        int textColor = weatherTypes.get(position)[0].getTextColor();
        if (cardColor == R.color.colorClearNightCard) cardColor = R.color.colorSunnyCard;
        if (textColor == R.color.colorClearNightText) textColor = R.color.colorSunnyText;
        cardColor = ContextCompat.getColor(holder.itemView.getContext(), cardColor);
        textColor = ContextCompat.getColor(holder.itemView.getContext(), textColor);
        ((CardView) ((LinearLayout) holder.itemView).getChildAt(0)).setCardBackgroundColor(cardColor);
        holder.expandTv.setTextColor(textColor);
        holder.iconDay.setTextColor(textColor);
        holder.iconEvening.setTextColor(textColor);
        holder.iconMorning.setTextColor(textColor);
        holder.iconNight.setTextColor(textColor);
        holder.iconMain.setTextColor(textColor);
        holder.iconDay.setTextColor(textColor);
        holder.tempMain.setTextColor(textColor);
        holder.tempDay.setTextColor(textColor);
        holder.tempEvening.setTextColor(textColor);
        holder.tempMorning.setTextColor(textColor);
        holder.tempNight.setTextColor(textColor);
        holder.tempExtreme.setTextColor(textColor);
        holder.date.setTextColor(textColor);
        holder.tvDay.setTextColor(textColor);
        holder.tvEvening.setTextColor(textColor);
        holder.tvNight.setTextColor(textColor);
        holder.tvMorning.setTextColor(textColor);
        holder.tvMin.setTextColor(textColor);
        holder.tvMax.setTextColor(textColor);
    }

    @SuppressWarnings("ConstantConditions")
    private void updateContentSize(RecyclerViewHolder holder, boolean newStateIsCollapsed, boolean isExpandable) {
        int visibility = newStateIsCollapsed ? View.GONE : View.VISIBLE;
        holder.tvMorning.setVisibility(visibility);
        holder.tvDay.setVisibility(visibility);
        holder.tvEvening.setVisibility(visibility);
        holder.tvNight.setVisibility(visibility);
        holder.iconMorning.setVisibility(visibility);
        holder.iconDay.setVisibility(visibility);
        holder.iconEvening.setVisibility(visibility);
        holder.iconNight.setVisibility(visibility);
        holder.tempMorning.setVisibility(visibility);
        holder.tempDay.setVisibility(visibility);
        holder.tempEvening.setVisibility(visibility);
        holder.tempNight.setVisibility(visibility);
        if (!newStateIsCollapsed) {
            holder.expandTv.setVisibility(View.GONE);
        } else {
            holder.expandTv.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        }
    }

    private String obtainTemperatureTitle(Resources resources, int tempUnits) {
        boolean isCelsius = tempUnits == ConvertersConfig.TEMPERATURE_CELSIUS;
        return resources.getString(isCelsius ? R.string.all_weather_celsius : R.string.all_weather_fahrenheit);
    }

    @Override
    public int getItemCount() {
        return weatherModels.size() + API_LICENCE_COUNT;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content_forecast_expand) @Nullable TextView expandTv;
        @BindView(R.id.content_forecast_date) @Nullable TextView date;
        @BindView(R.id.content_forecast_temp_night) @Nullable TextView tempNight;
        @BindView(R.id.content_forecast_temp_morning) @Nullable TextView tempMorning;
        @BindView(R.id.content_forecast_temp_main) @Nullable TextView tempMain;
        @BindView(R.id.content_forecast_temp_extreme) @Nullable TextView tempExtreme;
        @BindView(R.id.content_forecast_temp_evening) @Nullable TextView tempEvening;
        @BindView(R.id.content_forecast_temp_day) @Nullable TextView tempDay;
        @BindView(R.id.content_forecast_icon_day) @Nullable TextView iconDay;
        @BindView(R.id.content_forecast_icon_night) @Nullable TextView iconNight;
        @BindView(R.id.content_forecast_icon_morning) @Nullable TextView iconMorning;
        @BindView(R.id.content_forecast_icon_evening) @Nullable TextView iconEvening;
        @BindView(R.id.content_forecast_icon_main) @Nullable TextView iconMain;
        @BindView(R.id.content_forecast_tv_day) @Nullable TextView tvDay;
        @BindView(R.id.content_forecast_tv_evening) @Nullable TextView tvEvening;
        @BindView(R.id.content_forecast_tv_morning) @Nullable TextView tvMorning;
        @BindView(R.id.content_forecast_tv_night) @Nullable TextView tvNight;
        @BindView(R.id.content_forecast_max) @Nullable TextView tvMax;
        @BindView(R.id.content_forecast_min) @Nullable TextView tvMin;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
