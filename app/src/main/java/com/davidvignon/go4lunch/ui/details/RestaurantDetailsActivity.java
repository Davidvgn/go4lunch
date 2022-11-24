package com.davidvignon.go4lunch.ui.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.databinding.RestaurantDetailsActivityBinding;
import com.davidvignon.go4lunch.ui.map.MapViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity {

    private static final String KEY_PLACE_ID = "KEY_PLACE_ID";

    private String phoneNumber;

    public static Intent navigate(
        @NonNull Context context,
        @NonNull String placeId) {
        Intent intent = new Intent(context, RestaurantDetailsActivity.class);
        intent.putExtra(KEY_PLACE_ID, placeId);
        return intent;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RestaurantDetailsActivityBinding binding = RestaurantDetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String placeId = getIntent().getStringExtra(KEY_PLACE_ID);

        RestaurantDetailsViewModel viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);

        viewModel.getRestaurantDetailsViewStateLiveData(placeId).observe(this, new Observer<RestaurantDetailsViewState>() {
            @Override
            public void onChanged(RestaurantDetailsViewState restaurantDetailsViewState) {
                binding.restaurantDetailsTvName.setText(restaurantDetailsViewState.getName());
                binding.restaurantDetailsTvAddress.setText(restaurantDetailsViewState.getVicinity());
                binding.restaurantDetailsRb.setRating(restaurantDetailsViewState.getRating());
//          binding.restaurantDetailsBtCall.setText(restaurantDetailsViewState.getPhoneNumber());
//                 binding.restaurantDetailsBtWebsite.setText(restaurantDetailsViewState.getWebsite());

                String API_KEY = "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8"; //todo david to hide
                String restaurantPicture = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                    + restaurantDetailsViewState.getPhotosItemResponse() +
                    "&key=" + API_KEY;

                Glide.with(getApplicationContext())
                    .load(restaurantPicture)
                    .into(binding.restaurantDetailsIvHeader);
            }
        });


        binding.restaurantDetailsBtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "123456789"));
                startActivity(intentDial);
            }
        });
    }
}
