package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.RecyclerViewHolder> {
    private List<Place> places;

    public PlacesRecyclerAdapter(List<Place> places) {
        this.places = places;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, null));
    }

    @Override
    public void onBindViewHolder(PlacesRecyclerAdapter.RecyclerViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.tvPlaceName.setText(R.string.add_place);
            holder.ivColor.setImageResource(R.drawable.ic_action_add);
        } else {
            holder.tvFirstLetter.setText(places.get(position).getName().charAt(0));
            holder.tvPlaceName.setText(places.get(position).getName());
            holder.ivColor.setImageResource(R.color.colorAccent);
        }
    }

    @Override
    public int getItemCount() {
        return places.size() + 1; // + 1 for static "add" button
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_place_img) ImageView ivColor;
        @BindView(R.id.item_place_first_letter) TextView tvFirstLetter;
        @BindView(R.id.item_place_name) TextView tvPlaceName;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
