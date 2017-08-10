package sasd97.java_blog.xyz.yandexweather.presentation.navigation;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import sasd97.java_blog.xyz.yandexweather.R;
import sasd97.java_blog.xyz.yandexweather.data.models.places.Place;
import sasd97.java_blog.xyz.yandexweather.utils.AndroidMath;
import sasd97.java_blog.xyz.yandexweather.utils.FlipLayout;
import sasd97.java_blog.xyz.yandexweather.utils.SerializableSparseArray;

/**
 * Created by Maksim Sukhotski on 4/22/2017.
 */

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.RecyclerViewHolder> {
    public static final int TYPE_ADD_NEW_PLACE = 1;
    public static final int TYPE_PLACE = 0;
    private List<Place> places;
    private SerializableSparseArray<Place> selectedPlaces;
    private OnAddPlaceClickListener onAddPlaceClickListener;
    private OnPlaceSelectListener onPlaceSelectListener;

    public PlacesRecyclerAdapter setOnAddPlaceClickListener(OnAddPlaceClickListener addPlaceClickListener) {
        this.onAddPlaceClickListener = addPlaceClickListener;
        return this;
    }

    public PlacesRecyclerAdapter setOnPlaceSelectListener(OnPlaceSelectListener onPlaceSelectListener) {
        this.onPlaceSelectListener = onPlaceSelectListener;
        if (selectedPlaces == null) this.selectedPlaces = new SerializableSparseArray<>();
        return this;
    }

    public void cancelSelection() {
        selectedPlaces.clear();
    }

    public void removeSelectedPlaces() {
        for (int i = 0; i < selectedPlaces.size(); i++) {
            int positionToRemove = selectedPlaces.keyAt(i) - i ;
            notifyItemRemoved(positionToRemove);
            notifyItemRangeChanged(positionToRemove, places.size());
            places.remove(positionToRemove);
        }
        selectedPlaces.clear();
    }

    public void insertPlace(Place place) {
        places.add(0, place);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, places.size());
    }

    public interface OnAddPlaceClickListener {
        void onAddPlaceClick();
    }

    public interface OnPlaceSelectListener {
        void onSelectedPlace(int size);
    }

    public PlacesRecyclerAdapter(List<Place> places) {
        this.places = places;
    }

    public PlacesRecyclerAdapter(SerializableSparseArray<Place> selectedPlaces) {
        this.selectedPlaces = selectedPlaces;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int item_place = viewType == TYPE_PLACE ? R.layout.item_place : R.layout.item_add_place;
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(item_place, null));
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_ADD_NEW_PLACE : TYPE_PLACE;
    }

    @Override
    public void onBindViewHolder(PlacesRecyclerAdapter.RecyclerViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.tvPlaceName.setText(R.string.add_place);
            holder.itemView.setOnClickListener(view -> onAddPlaceClickListener.onAddPlaceClick());
        } else {
            assert holder.flipLayout != null;
            assert holder.tvFirstLetter != null;
            holder.tvFirstLetter.setText(String.valueOf(places.get(position).getName().charAt(0)));
            int hashCode = places.get(position).hashCode();
            holder.ivColor.setColorFilter(holder.colors[AndroidMath.intToDigit(hashCode)]);
            holder.tvPlaceName.setText(places.get(position).getName());
            if (selectedPlaces.get(position) != null) holder.flipLayout.setFlipped(true);
            else holder.flipLayout.setFlipped(false);
            holder.flipLayout.setOnSelectedListener(selected -> {
                if (selected) selectedPlaces.put(position, places.get(position));
                else selectedPlaces.remove(position);
                onPlaceSelectListener.onSelectedPlace(selectedPlaces.size());
            });
        }
    }

    @Override
    public int getItemCount() {
        return places.size() + 1; // + 1 for static "add" button
    }

    public List<Place> getPlaces() {
        return places;
    }

    public SerializableSparseArray<Place> getSelectedPlaces() {
        return selectedPlaces;
    }

    public void setSelectedPlaces(SerializableSparseArray<Place> selectedPlaces) {
        this.selectedPlaces = selectedPlaces;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_place_first_letter) @Nullable TextView tvFirstLetter;
        @BindView(R.id.item_place_name) TextView tvPlaceName;
        @BindView(R.id.item_place_img) ImageView ivColor;
        @BindView(R.id.item_place_flip_layout) @Nullable FlipLayout flipLayout;
        @BindArray(R.array.rainbow) int[] colors;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
