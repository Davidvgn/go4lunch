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
import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.databinding.RestaurantDetailsActivityBinding;
import com.davidvignon.go4lunch.ui.map.MapViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity {

    @NonNull
    private String phoneNumber;

    @NonNull
    private String website;

    String restaurantName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RestaurantDetailsActivityBinding binding = RestaurantDetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RestaurantDetailsViewModel viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);

        viewModel.getRestaurantDetailsViewStateLiveData().observe(this, new Observer<RestaurantDetailsViewState>() {
            @Override
            public void onChanged(RestaurantDetailsViewState restaurantDetailsViewState) {
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
            }
        });

            binding.restaurantDetailsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                binding.restaurantDetailsFab.setImageResource(R.drawable.ic_baseline_check_circle_24);
            }
        });

        binding.restaurantDetailsBtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        binding.restaurantDetailsBtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(intentDial);
            }
        });

        binding.restaurantDetailsBtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = website;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
