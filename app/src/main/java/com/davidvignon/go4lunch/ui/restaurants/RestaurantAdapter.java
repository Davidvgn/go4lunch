package com.davidvignon.go4lunch.ui.restaurants;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.databinding.RestaurantsItemviewBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//todo david utiliser hilt
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

        @SuppressLint("RestrictedApi")
        public void bind(RestaurantViewState item, OnRestaurantClickedListener listener) {
            binding.restaurantItemTvName.setText(item.getName());
            binding.restaurantItemTvAdress.setText(item.getVicinity());
//            Glide.with(binding.restaurantItemIvPicture.getContext())
//                .load(item.getPhotos())
//                .into(binding.restaurantItemIvPicture);

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
