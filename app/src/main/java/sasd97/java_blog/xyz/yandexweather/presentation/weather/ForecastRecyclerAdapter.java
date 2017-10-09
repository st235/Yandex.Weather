package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.domain.converters.ConvertersConfig;
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;
import sasd97.java_blog.xyz.yandexweather.utils.Settings;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.RecyclerViewHolder> {
    public static final int TYPE_COLLAPSED = 0;
    public static final int TYPE_EXPANDED = 1;
    private final Map<WeatherModel, WeatherType[]> forecasts;
    private final Settings settings;
    private final boolean isSecondary;

    public ForecastRecyclerAdapter(Map<WeatherModel, WeatherType[]> forecasts,
                                   Settings settings) {
        this.forecasts = forecasts;
        this.settings = settings;
        this.isSecondary = false;
    }

    public ForecastRecyclerAdapter(Map<WeatherModel, WeatherType[]> forecasts,
                                   Settings settings, boolean isSecondary) {
        this.forecasts = forecasts;
        this.settings = settings;
        this.isSecondary = isSecondary;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_COLLAPSED || isSecondary) {
            return new RecyclerViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_forecast_collapsed, parent, false));
        } else {
            return new RecyclerViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_forecast_expanded, parent, false));
        }

    }

    @Override
    public int getItemViewType(int position) {
        WeatherModel weather = (WeatherModel) forecasts.keySet().toArray()[position];
        if (position > forecasts.get(weather).length) return TYPE_COLLAPSED;
        else return TYPE_EXPANDED;
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecyclerViewHolder holder, int position) {
        WeatherModel weather = (WeatherModel) forecasts.keySet().toArray()[position];
        Resources resources = holder.itemView.getResources();
        setTheme(holder, weather);
        setData(holder, position, weather, resources);
    }

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
        int iconMorning = forecasts.get(weather)[0].getIconRes();
        if (iconMorning == R.string.all_weather_clear_night_icon)
            iconMorning = R.string.all_weather_sunny_icon;
        if (position > forecasts.get(weather).length || forecasts.get(weather)[1] == null) {
            /*set icon morning because forecast16 response contains only 1 weather type for whole day*/
            /*and we put it in [0] position in array*/
            holder.iconMain.setText(iconMorning);
        } else {
            int iconDay = forecasts.get(weather)[1].getIconRes();
            int iconEveninig = forecasts.get(weather)[2].getIconRes();
            if (iconDay == R.string.all_weather_clear_night_icon)
                iconDay = R.string.all_weather_sunny_icon;
            if (iconEveninig == R.string.all_weather_clear_night_icon)
                iconEveninig = R.string.all_weather_sunny_icon;
            holder.iconMain.setText(iconDay);
            holder.iconMorning.setText(iconMorning);
            holder.iconDay.setText(iconDay);
            holder.iconEvening.setText(iconEveninig);
            if (forecasts.get(weather)[3] == null) {
                forecasts.get(weather)[3] = forecasts.get(weather)[2];
            }
            holder.iconNight.setText(forecasts.get(weather)[3].getIconRes());
        }
    }

    private void setTheme(RecyclerViewHolder holder, WeatherModel weather) {
        boolean isDetailed = forecasts.get(weather)[1] != null;
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
