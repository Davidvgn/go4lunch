package com.davidvignon.go4lunch.ui.details;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.davidvignon.go4lunch.databinding.RestaurantDetailsActivityBinding;
import com.davidvignon.go4lunch.ui.OnWorkmateClickedListener;
import com.davidvignon.go4lunch.ui.chat.ChatViewModel;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity implements OnWorkmateClickedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RestaurantDetailsActivityBinding binding = RestaurantDetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WorkmatesAdapter adapter = new WorkmatesAdapter(this);
        binding.workmatesRv.setAdapter(adapter);
        setSupportActionBar(binding.restaurantDetailsToolbar);
        binding.restaurantDetailsToolbar.setTitleTextColor(Color.WHITE);

        RestaurantDetailsViewModel viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);

        viewModel.getRestaurantDetailsViewStateLiveData().observe(this, restaurantDetailsViewState -> {
                binding.restaurantDetailsTvName.setText(restaurantDetailsViewState.getName());
                binding.restaurantDetailsTvAddress.setText(restaurantDetailsViewState.getVicinity());
                binding.restaurantDetailsRb.setRating(restaurantDetailsViewState.getRating());

                Glide.with(getApplicationContext())
                    .load(viewModel.getRestaurantPicture(restaurantDetailsViewState))
                    .into(binding.restaurantDetailsIvHeader);

                binding.restaurantDetailsBtCall.setOnClickListener(v -> {
                    Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse(viewModel.getRestaurantPhoneNumber(restaurantDetailsViewState)));
                    startActivity(intentDial);
                });

                binding.restaurantDetailsBtLike.setText(restaurantDetailsViewState.getLikeButtonText());

                binding.restaurantDetailsBtLike.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    ResourcesCompat.getDrawable(getResources(), restaurantDetailsViewState.getLikeButtonIcon(), null),
                    null,
                    null
                );

                binding.restaurantDetailsFab.setImageResource(restaurantDetailsViewState.getSelectedButtonIcon());

                binding.restaurantDetailsBtLike.setOnClickListener(v -> {
                    viewModel.onLikedButtonClicked();
                    binding.restaurantDetailsBtLike.setCompoundDrawables(
                        null,
                        ResourcesCompat.getDrawable(getResources(), restaurantDetailsViewState.getLikeButtonIcon(), null),
                        null,
                        null
                    );
                });

                binding.restaurantDetailsBtWebsite.setOnClickListener(view -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(restaurantDetailsViewState.getWebsite()));
                    startActivity(i);
                });

                binding.restaurantDetailsFab.setOnClickListener(view -> viewModel.selectRestaurant());

                adapter.submitList(viewModel.getWorkmatesViewStates().getValue());

            }
        );
    }

    @Override
    public void onWorkmateClicked(String workmateId) {
        startActivity(ChatViewModel.navigate(this, workmateId));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
