package com.davidvignon.go4lunch.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.databinding.MainActivityBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsActivity;
import com.davidvignon.go4lunch.ui.dispatcher.DispatcherViewAction;
import com.davidvignon.go4lunch.ui.map.MapFragment;
import com.davidvignon.go4lunch.ui.oauth.OAuthActivity;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsFragment;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnRestaurantClickedListener {

    @Inject
    LocationRepository locationRepository;

    @Inject
    PermissionRepository permissionRepository;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private MainViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.header_user_name);
        ImageView navUserImage = (ImageView) headerView.findViewById(R.id.header_iv);
        TextView navUseremail = (TextView) headerView.findViewById(R.id.header_user_email);


        Toolbar toolbar = binding.mainToolBar;
        toolbar.setTitle(R.string.restaurantViewTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawerLayout, R.string.nav_open, R.string.nav_close);
        binding.mainDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        //FACEBOOK
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
        accessToken,
            (object, response) -> {
                // Application code
                try {
                    String fullName = object.getString("name");
                    String email = object.getString("email");

                    String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Log.i("DavidVgn", "onCreate: "+url);
                    navUsername.setText(fullName);
                    navUseremail.setText(email);
                    Glide.with(MainActivity.this)
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(navUserImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name, email,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

        //GOOGLE
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            navUsername.setText(personName);
            navUseremail.setText(personEmail);
            Glide.with(MainActivity.this)
                .load(personPhoto)
                .apply(RequestOptions.circleCropTransform())
                .into(navUserImage);
        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainDrawerLayout.open();
            }
        });


        binding.mainNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.nav_lunch):
                        return true;
                    case (R.id.nav_settings):
                        return true;
                    case (R.id.nav_logout):
                        LoginManager.getInstance().logOut();
                        //TODO NINO : Si : logout -> fermeture de l'app -> relancement de l'app -> je ne reviens pas sur l'écran de connection (gérer le dispatcher)
                        startActivity(new Intent(MainActivity.this, OAuthActivity.class));
                        return true;
                }
                binding.mainDrawerLayout.close();
                return true;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_FrameLayout_fragment_container, MapFragment.newInstance())
                .commit();
        }

        binding.mainBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.bottom_nav_map):
                        displayFragment(MapFragment.newInstance());
                        toolbar.setTitle(R.string.restaurantViewTitle);
                        break;
                    case (R.id.bottom_nav_list):
                        displayFragment(RestaurantsFragment.newInstance());
                        toolbar.setTitle(R.string.restaurantViewTitle);
                        break;
                    case (R.id.bottom_nav_workmates):
                        displayFragment(WorkmatesFragment.newInstance());
                        toolbar.setTitle(R.string.workermatesViewTitle);
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
    public void onItemClick(String placeId) {
        startActivity(RestaurantDetailsActivity.navigate(this, placeId));
    }

}