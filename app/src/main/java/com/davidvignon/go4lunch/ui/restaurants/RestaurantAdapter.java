package com.davidvignon.go4lunch.ui.restaurants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.davidvignon.go4lunch.databinding.RestaurantsItemviewBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;

public class RestaurantAdapter extends ListAdapter<RestaurantViewState, RestaurantAdapter.ViewHolder> {

    @NonNull
    private final OnRestaurantClickedListener listener;


    public RestaurantAdapter(@NonNull OnRestaurantClickedListener listener) {
        super(new ListRestaurantItemCallBack());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RestaurantsItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RestaurantsItemviewBinding binding;

        public ViewHolder(@NonNull RestaurantsItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RestaurantViewState item, OnRestaurantClickedListener listener) {
            String photoReference = item.getPhotosItemResponse();
            String API_KEY = "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8"; //todo david to hide
            String restaurantPicture = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + photoReference +
                "&key=" + API_KEY;

            binding.restaurantItemTvName.setText(item.getName());
            binding.restaurantItemTvAdress.setText(item.getVicinity());
            binding.restaurantItemTvClosureHour.setText(item.getOpenOrClose());
            binding.restaurantItemRb.setRating(item.getRating());
            binding.restaurantItemTvDistance.setText(item.getDistance());

            Glide.with(binding.restaurantItemIvPicture.getContext())
                .load(restaurantPicture)
                .into(binding.restaurantItemIvPicture);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onItemClick(item.getName(), item.getVicinity(), item.getPhotosItemResponse(), item.getRating());
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item.getPlaceId());
                }
            });
        }
    }

    private static class ListRestaurantItemCallBack extends DiffUtil.ItemCallback<RestaurantViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull RestaurantViewState oldItem, @NonNull RestaurantViewState newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RestaurantViewState oldItem, @NonNull RestaurantViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
