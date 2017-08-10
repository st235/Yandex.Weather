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
import sasd97.java_blog.xyz.yandexweather.domain.models.WeatherModel;
import sasd97.java_blog.xyz.yandexweather.presentation.weatherTypes.WeatherType;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.RecyclerViewHolder> {
    private LinkedHashMap<WeatherModel, WeatherType> forecasts;

    public ForecastRecyclerAdapter(LinkedHashMap<WeatherModel, WeatherType> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecyclerViewHolder holder, int position) {
        holder.date.setText(getReadableDateAt(position, holder.itemView.getResources()));
//        holder.iconMain.
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public String getReadableDateAt(int position, Resources resources) {
        if (position == 0) return resources.getString(R.string.tomorrow);
        if (position == 1) return resources.getString(R.string.day_after_tomorrow);
        return ((WeatherModel) forecasts.keySet().toArray()[position]).getReadableDate();
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
