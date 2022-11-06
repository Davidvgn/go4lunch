package com.davidvignon.go4lunch.ui.restaurants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;
import com.davidvignon.go4lunch.databinding.RestaurantsItemviewBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, RestaurantDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RestaurantsItemviewBinding binding;

        public ViewHolder(@NonNull RestaurantsItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("RestrictedApi")
        public void bind(RestaurantViewState item, OnRestaurantClickedListener listener) {
            String photoreference = item.getPhotosItemResponse();
            String API_KEY = "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8";
            String restaurantpic =  "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                 +photoreference +
                "&key="+API_KEY;

            binding.restaurantItemTvName.setText(item.getName());
            binding.restaurantItemTvAdress.setText(item.getVicinity());
            Glide.with(binding.restaurantItemIvPicture.getContext())
                .load(restaurantpic) //todo david
                .into(binding.restaurantItemIvPicture);

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
