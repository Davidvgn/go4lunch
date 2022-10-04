package com.davidvignon.go4lunch.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.MainActivityBinding;
import com.davidvignon.go4lunch.ui.map.MapFragment;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsFragment;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public ActionBarDrawerToggle actionBarDrawerToggle;
    MainActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.mainToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawerLayout, R.string.nav_open, R.string.nav_close);
        binding.mainDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


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
                        displayFragment(MapFragment.newInstance());
                        break;
                    case (R.id.bottom_nav_list):
                        displayFragment(RestaurantsFragment.newInstance());
                        break;
                    case (R.id.bottom_nav_workmates):
                        displayFragment(WorkmatesFragment.newInstance());
                        break;
                }
                return true;
            }
        });
    }

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_FrameLayout_fragment_container, fragment, null)
            .commitNow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Tapez votre recherche");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}