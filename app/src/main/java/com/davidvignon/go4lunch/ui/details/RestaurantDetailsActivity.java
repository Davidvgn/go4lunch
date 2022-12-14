package com.davidvignon.go4lunch.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.davidvignon.go4lunch.databinding.RestaurantDetailsActivityBinding;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity {

    @NonNull
    private String phoneNumber;

    @NonNull
    private String website;

    String restaurantName;

    boolean isFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RestaurantDetailsActivityBinding binding = RestaurantDetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WorkmatesAdapter adapter = new WorkmatesAdapter();
        binding.workmatesRv.setAdapter(adapter);


        RestaurantDetailsViewModel viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);

        viewModel.getRestaurantDetailsViewStateLiveData().observe(this, restaurantDetailsViewState -> {
            binding.restaurantDetailsTvName.setText(restaurantDetailsViewState.getName());
            binding.restaurantDetailsTvAddress.setText(restaurantDetailsViewState.getVicinity());
            binding.restaurantDetailsRb.setRating(restaurantDetailsViewState.getRating());
            phoneNumber = restaurantDetailsViewState.getPhoneNumber();
            website = restaurantDetailsViewState.getWebsite();

            String API_KEY = "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8"; //todo david to hide
            String restaurantPicture = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + restaurantDetailsViewState.getPhotosItemResponse() +
                "&key=" + API_KEY;

            Glide.with(getApplicationContext())
                .load(restaurantPicture)
                .into(binding.restaurantDetailsIvHeader);

            restaurantName = restaurantDetailsViewState.getName();
        });

            binding.restaurantDetailsFab.setOnClickListener(view -> viewModel.selectRestaurant());

        binding.restaurantDetailsBtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.restaurantIsFavoriteOrNot();

            }
        });

        viewModel.getWorkmatesViewStatesLiveData().observe(this, workmatesViewStates -> adapter.submitList(workmatesViewStates));

        binding.restaurantDetailsBtCall.setOnClickListener(v -> {
            Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intentDial);
        });

        binding.restaurantDetailsBtWebsite.setOnClickListener(view -> {
            String url = website;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }

}
