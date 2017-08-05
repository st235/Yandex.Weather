package sasd97.java_blog.xyz.yandexweather.presentation.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sasd97.java_blog.xyz.yandexweather.R;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.RecyclerViewHolder> {
    private List<Forecast> forecasts;
//
//    public ForecastRecyclerAdapter(List<Forecast> forecasts) {
//        this.forecasts = forecasts;
//    }


    public ForecastRecyclerAdapter() {
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, null));
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecyclerViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 17;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView favoriteImageView;
        TextView originalTextView;
        TextView targetTextView;
        TextView dirTextView;
        LinearLayout linearLayout;

        RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
