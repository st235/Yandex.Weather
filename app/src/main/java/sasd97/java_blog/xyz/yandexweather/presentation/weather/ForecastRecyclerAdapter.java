package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
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
import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.RecyclerViewHolder> {
    private static final int TYPE_EXPANDED = 1;
    private static final int DETAILED_DAY_COUNT = 5;
    private final Map<WeatherModel, WeatherType[]> forecasts;
    private final Settings settings;
    private final boolean isSecondary;

    private RecyclerView recyclerView;

    ForecastRecyclerAdapter(Map<WeatherModel, WeatherType[]> forecasts,
                            Settings settings) {
        this.forecasts = forecasts;
        this.settings = settings;
        this.isSecondary = false;
    }

    private void updateContentSize(View view) {
        boolean newStateIsCollapsed = view.findViewById(R.id.content_forecast_tv_morning).getVisibility() == View.VISIBLE;
        int visibility = newStateIsCollapsed ? View.GONE : View.VISIBLE;
        view.findViewById(R.id.content_forecast_tv_morning).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_tv_day).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_tv_evening).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_tv_night).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_icon_morning).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_icon_day).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_icon_evening).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_icon_night).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_temp_morning).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_temp_day).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_temp_evening).setVisibility(visibility);
        view.findViewById(R.id.content_forecast_temp_night).setVisibility(visibility);
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

    public ForecastRecyclerAdapter(Map<WeatherModel, WeatherType[]> forecasts,
                                   Settings settings, boolean isSecondary) {
        this.forecasts = forecasts;
        this.settings = settings;
        this.isSecondary = isSecondary;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @LayoutRes int layoutRes = isSecondary ?
                R.layout.item_forecast_expanded : R.layout.item_forecast_collapsed;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);
//        itemView.setOnClickListener(view -> {
//            boolean newStateIsExpanded = view.findViewById(R.id.content_forecast_tv_morning).getVisibility() == View.VISIBLE;
//
//            TransitionSet transition = new TransitionSet()
//                    .addTransition(new ChangeBounds())
//                    .addTransition(new Explode())
//                    .addTransition((new Fade()).setDuration(newStateIsExpanded ? 75 : 400));
//
//            TransitionManager.beginDelayedTransition(recyclerView);
//
//            updateContentSize(view);
//        });

        return new RecyclerViewHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_EXPANDED;
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecyclerViewHolder holder, int position) {
        WeatherModel weather = (WeatherModel) forecasts.keySet().toArray()[position];
        Resources resources = holder.itemView.getResources();
        setTheme(holder, weather);
        setData(holder, position, weather, resources);

        RxView.clicks(holder.itemView)
                .throttleFirst(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(ignore -> {
                    TransitionManager.beginDelayedTransition(recyclerView, new TransitionSet()
                            .addTransition(new ChangeBounds())
                            .addTransition(new Fade())
                            .addTransition(new Explode()));
                    updateContentSize(holder.itemView);
                }, Throwable::printStackTrace);
    }

    /**
     * Set icon morning because forecast16 response contains only 1 weather type for whole day,
     * and we put it in [0] position in array.
     */
    private void setData(RecyclerViewHolder holder, int position, WeatherModel weather, Resources resources) {
        holder.date.setText(position == 0 && !isSecondary ? resources.getString(R.string.tomorrow) : weather.getReadableDate());
        holder.tempMain.setText(resources.getString(R.string.forecasts_temperature,
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
        boolean isIconifiedForecast = forecasts.get(weather).length == 5;
        int iconVisibility = isIconifiedForecast ? View.VISIBLE : View.GONE;
        holder.iconMorning.setVisibility(iconVisibility);
        holder.iconDay.setVisibility(iconVisibility);
        holder.iconEvening.setVisibility(iconVisibility);
        holder.iconNight.setVisibility(iconVisibility);
        if (isIconifiedForecast) {
            holder.iconMain.setText(forecasts.get(weather)[0].getIconRes());
            holder.iconMorning.setText(forecasts.get(weather)[1].getIconRes());
            holder.iconDay.setText(forecasts.get(weather)[2].getIconRes());
            holder.iconEvening.setText(forecasts.get(weather)[3].getIconRes());
            holder.iconNight.setText(forecasts.get(weather)[4].getIconRes());
        }
        holder.iconMain.setText(forecasts.get(weather)[0].getIconRes());
    }

    private void setTheme(RecyclerViewHolder holder, WeatherModel weather) {
        boolean isDetailed = forecasts.get(weather).length == DETAILED_DAY_COUNT;
        int cardColor = forecasts.get(weather)[isDetailed ? 1 : 0].getCardColor();
        int textColor = forecasts.get(weather)[isDetailed ? 1 : 0].getTextColor();
        if (cardColor == R.color.colorClearNightCard) cardColor = R.color.colorSunnyCard;
        if (textColor == R.color.colorClearNightText) textColor = R.color.colorSunnyText;
        cardColor = ContextCompat.getColor(holder.itemView.getContext(), cardColor);
        textColor = ContextCompat.getColor(holder.itemView.getContext(), textColor);
        ((CardView) ((LinearLayout) holder.itemView).getChildAt(0)).setCardBackgroundColor(cardColor);
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
    }

    private String obtainTemperatureTitle(Resources resources, int tempUnits) {
        boolean isCelsius = tempUnits == ConvertersConfig.TEMPERATURE_CELSIUS;
        return resources.getString(isCelsius ? R.string.all_weather_celsius : R.string.all_weather_fahrenheit);
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content_forecast_date) TextView date;
        @BindView(R.id.content_forecast_temp_night) TextView tempNight;
        @BindView(R.id.content_forecast_temp_morning) TextView tempMorning;
        @BindView(R.id.content_forecast_temp_main) TextView tempMain;
        @BindView(R.id.content_forecast_temp_extreme) TextView tempExtreme;
        @BindView(R.id.content_forecast_temp_evening) TextView tempEvening;
        @BindView(R.id.content_forecast_temp_day) TextView tempDay;
        @BindView(R.id.content_forecast_icon_day) TextView iconDay;
        @BindView(R.id.content_forecast_icon_night) TextView iconNight;
        @BindView(R.id.content_forecast_icon_morning) TextView iconMorning;
        @BindView(R.id.content_forecast_icon_evening) TextView iconEvening;
        @BindView(R.id.content_forecast_icon_main) TextView iconMain;
        @BindView(R.id.content_forecast_tv_day) TextView tvDay;
        @BindView(R.id.content_forecast_tv_evening) TextView tvEvening;
        @BindView(R.id.content_forecast_tv_morning) TextView tvMorning;
        @BindView(R.id.content_forecast_tv_night) TextView tvNight;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
