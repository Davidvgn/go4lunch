package com.davidvignon.go4lunch.ui.details;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.davidvignon.go4lunch.databinding.RestaurantDetailsActivityBinding;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantViewState;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private RestaurantDetailsActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RestaurantDetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.restaurantDetailsTvAddress.setText("Adresse pour test");
        binding.restaurantDetailsTvName.setText("Name pour test");


        Glide.with(this)
            .load("https://i.pravatar.cc/150?u=a042581f4e29026704d") //todo david charger la bonne image
            .into(binding.restaurantDetailsIvHeader);
    }
}
