package com.davidvignon.go4lunch.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.MainActivityBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;
import com.davidvignon.go4lunch.ui.OnWorkmateClickedListener;
import com.davidvignon.go4lunch.ui.chat.ChatViewModel;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsViewModel;
import com.davidvignon.go4lunch.ui.map.MapFragment;
import com.davidvignon.go4lunch.ui.oauth.OAuthActivity;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsFragment;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnRestaurantClickedListener, OnWorkmateClickedListener {

    private MainActivityBinding binding;
    private MainViewModel viewModel;
    //todo david notification
    //todo david redirection vers resto quand click sur marker
    //todo david repositionnement de la carte quand click sur icone + un border why not...
    //todo david UNIT TEST
    //todo david gérer les couleurs
    //todo david gérer la traduction des strings
    //todo david warnings
//todo david tronquer dans workmateslist si nom e restaurant trop long
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.header_user_name);
        ImageView navUserImage = headerView.findViewById(R.id.header_iv);
        TextView navUseremail = headerView.findViewById(R.id.header_user_email);

        Toolbar toolbar = binding.mainToolBar;
        toolbar.setTitle(R.string.restaurantViewTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawerLayout, R.string.nav_open, R.string.nav_close);
        binding.mainDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        viewModel.getNotificationDialogSingleLiveEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Notifications disabled");// todo david  all text in strings
            builder.setMessage("Please enable notifications in the settings to receive updates.");
            builder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
            }
        });

        binding.locationButton.setOnClickListener(new View.OnClickListener() {//todo david
            @Override
            public void onClick(View view) {

            }
        });

        viewModel.getPicturePathLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(MainActivity.this)
                    .load(s)
                    .apply(RequestOptions.circleCropTransform())
                    .into(navUserImage);
            }
        });

        viewModel.getNameAndEmailLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> stringList) {
                navUsername.setText(stringList.get(0));
                navUseremail.setText(stringList.get(1));
            }
        });

        toolbar.setNavigationOnClickListener(view -> binding.mainDrawerLayout.open());

        binding.mainNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.nav_lunch):
                    viewModel.onMyLunchClicked();
                    return true;
                case (R.id.nav_settings):
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
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
                    displayFragment(MapFragment.newInstance());
                    toolbar.setTitle(R.string.restaurantViewTitle);
                    binding.locationButton.setVisibility(View.VISIBLE);
                    break;
                case (R.id.bottom_nav_list):
                    displayFragment(RestaurantsFragment.newInstance());
                    toolbar.setTitle(R.string.restaurantViewTitle);
                    binding.locationButton.setVisibility(View.INVISIBLE);
                    break;
                case (R.id.bottom_nav_workmates):
                    displayFragment(WorkmatesFragment.newInstance());
                    toolbar.setTitle(R.string.workermatesViewTitle);
                    binding.locationButton.setVisibility(View.INVISIBLE);
                    break;
            }
            return true;
        });

        viewModel.getMainActionLiveData().observe(this, new Observer<EventWrapper<MainAction>>() {
            @Override
            public void onChanged(EventWrapper<MainAction> wrapper) {
                MainAction mainAction = wrapper.getContentIfNotHandled();

                if (mainAction != null) {
                    if (mainAction instanceof MainAction.Toast) {
                        Toast.makeText(MainActivity.this, ((MainAction.Toast) mainAction).messageStringRes, Toast.LENGTH_SHORT).show();
                    } else if (mainAction instanceof MainAction.DetailNavigation) {
                        startActivity(RestaurantDetailsViewModel.navigate(MainActivity.this, ((MainAction.DetailNavigation) mainAction).placeId));
                    }
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
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.type_your_search));
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
}