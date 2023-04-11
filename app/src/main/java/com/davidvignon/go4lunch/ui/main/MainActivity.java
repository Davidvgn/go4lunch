package com.davidvignon.go4lunch.ui.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.HeaderBinding;
import com.davidvignon.go4lunch.databinding.MainActivityBinding;
import com.davidvignon.go4lunch.ui.OnPredictionClickedListener;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;
import com.davidvignon.go4lunch.ui.OnWorkmateClickedListener;
import com.davidvignon.go4lunch.ui.chat.ChatViewModel;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsViewModel;
import com.davidvignon.go4lunch.ui.map.MapFragment;
import com.davidvignon.go4lunch.ui.main.predictions.PredictionsAdapter;
import com.davidvignon.go4lunch.ui.settings.SettingsActivity;
import com.davidvignon.go4lunch.ui.oauth.OAuthActivity;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsFragment;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnRestaurantClickedListener,
    OnWorkmateClickedListener, OnPredictionClickedListener {
    private MainActivityBinding binding;
    private MainViewModel viewModel;

    SearchView searchView;
    PredictionsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        HeaderBinding headerBinding = HeaderBinding.bind(binding.mainNavigationView.getHeaderView(0));

        Toolbar toolbar = binding.mainToolBar;
        toolbar.setTitle(R.string.restaurantViewTitle);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawerLayout, R.string.nav_open, R.string.nav_close);
        binding.mainDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        adapter = new PredictionsAdapter(this);
        binding.predictionsRv.setAdapter(adapter);
        binding.predictionsRv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        viewModel.getMainViewStateLiveData().observe(this, state -> {
            Glide.with(headerBinding.headerIv)
                .load(state.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(headerBinding.headerIv);

            headerBinding.headerUserName.setText(state.getName());
            headerBinding.headerUserEmail.setText(state.getMail());
        });

        viewModel.getPredictionsViewStateLiveData().observe(this, predictionViewStates -> adapter.submitList(predictionViewStates));
        toolbar.setNavigationOnClickListener(view -> binding.mainDrawerLayout.open());

        binding.mainNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.nav_lunch):
                    viewModel.onMyLunchClicked();
                    return true;
                case (R.id.nav_settings):
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case (R.id.nav_logout):
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, OAuthActivity.class));
                    return true;
            }
            binding.mainDrawerLayout.close();
            return true;
        });

        if (savedInstanceState == null) {
            displayFragment(MapFragment.newInstance());
        }

        binding.mainBottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.bottom_nav_map):
                    MainActivity.this.displayFragment(MapFragment.newInstance());
                    toolbar.setTitle(R.string.restaurantViewTitle);
                    viewModel.onSearchedRestaurantSelected(null);
                    break;
                case (R.id.bottom_nav_list):
                    MainActivity.this.displayFragment(RestaurantsFragment.newInstance());
                    toolbar.setTitle(R.string.restaurantViewTitle);
                    viewModel.onSearchedRestaurantSelected(null);
                    break;
                case (R.id.bottom_nav_workmates):
                    MainActivity.this.displayFragment(WorkmatesFragment.newInstance());
                    toolbar.setTitle(R.string.workematesViewTitle);
                    viewModel.onSearchedRestaurantSelected(null);
                    break;
            }
            return true;
        });

        viewModel.getMainActionLiveData().observe(this, wrapper -> {
            MainAction mainAction = wrapper.getContentIfNotHandled();

            if (mainAction != null) {
                if (mainAction instanceof MainAction.Toast) {
                    Toast.makeText(MainActivity.this, ((MainAction.Toast) mainAction).messageStringRes, Toast.LENGTH_SHORT).show();
                } else if (mainAction instanceof MainAction.DetailNavigation) {
                    startActivity(RestaurantDetailsViewModel.navigate(MainActivity.this, ((MainAction.DetailNavigation) mainAction).placeId));
                }
            }
        });
    }

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_FrameLayout_fragment_container, fragment)
            .commitNow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.clearFocus();
        searchView.setQueryHint(getString(R.string.type_your_search));
        searchView.setIconifiedByDefault(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                viewModel.onSearchedRestaurantSelected(null);
                viewModel.getSearchQueryText(null);
                return true;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.onSearchedRestaurantSelected(query);
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                searchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                viewModel.getSearchQueryText(text);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    public void onBackPressed() {
        if (binding.mainDrawerLayout.isOpen()) {
            binding.mainDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRestaurantClicked(String placeId) {
        startActivity(RestaurantDetailsViewModel.navigate(this, placeId));
    }

    @Override
    public void onWorkmateClicked(String workmateId) {
        startActivity(ChatViewModel.navigate(this, workmateId));
    }

    @Override
    public void onPredictionClickedListener(String placeId, String description) {
        startActivity(RestaurantDetailsViewModel.navigate(this, placeId));
    }

}