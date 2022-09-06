package com.davidvignon.go4lunch.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.MainActivityBinding;
import com.davidvignon.go4lunch.ui.map.MapFragment;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsFragment;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.main_FrameLayout_fragment_container, MapFragment.newInstance())
                .commit();
        }

        binding.mainBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.bottom_nav_map):
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_FrameLayout_fragment_container, MapFragment.newInstance(), null)
                            .commitNow();
                        break;
                    case (R.id.bottom_nav_list):
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_FrameLayout_fragment_container, RestaurantsFragment.newInstance(), null)
                            .commitNow();
                        break;
                    case (R.id.bottom_nav_workmates):
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_FrameLayout_fragment_container, WorkmatesFragment.newInstance(), null)
                            .commitNow();
                        break;
                }
                return true;
            }
        });
    }
}