package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedHashMap;

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
    private final LinkedHashMap<WeatherModel, WeatherType[]> forecasts;
    private final Settings settings;

    public ForecastRecyclerAdapter(LinkedHashMap<WeatherModel, WeatherType[]> forecasts,
                                   Settings settings) {
        this.forecasts = forecasts;
        this.settings = settings;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecyclerViewHolder holder, int position) {
        WeatherModel weather = (WeatherModel) forecasts.keySet().toArray()[position];
        Resources resources = holder.itemView.getResources();
        holder.date.setText(position == 0 ? resources.getString(R.string.tomorrow) : weather.getReadableDate());
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
        if (iconMorning == R.string.all_weather_clear_night_icon) {
            iconMorning = R.string.all_weather_sunny_icon;
        }
        if (forecasts.get(weather).length <= position) {
            holder.iconMain.setText(iconMorning);
        } else {
            int iconDay = forecasts.get(weather)[1].getIconRes();
            int iconEveninig = forecasts.get(weather)[2].getIconRes();
            if (iconDay == R.string.all_weather_clear_night_icon) {
                iconDay = R.string.all_weather_sunny_icon;
            }
            if (iconEveninig == R.string.all_weather_clear_night_icon) {
                iconEveninig = R.string.all_weather_sunny_icon;
            }
            holder.iconMain.setText(iconDay);
            holder.iconMorning.setText(iconMorning);
            holder.iconDay.setText(iconDay);
            holder.iconEvening.setText(iconEveninig);
            holder.iconNight.setText(forecasts.get(weather)[3].getIconRes());
        }
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

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
