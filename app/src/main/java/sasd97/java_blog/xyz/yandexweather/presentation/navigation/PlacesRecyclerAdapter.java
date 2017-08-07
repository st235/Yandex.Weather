package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.RecyclerViewHolder> {
    private List<Place> forecasts;
//
//    public PlacesRecyclerAdapter(List<Forecast> forecasts) {
//        this.forecasts = forecasts;
//    }


    public PlacesRecyclerAdapter() {
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, null));
    }

    @Override
    public void onBindViewHolder(PlacesRecyclerAdapter.RecyclerViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 20;
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
