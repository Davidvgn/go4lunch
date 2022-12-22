package com.davidvignon.go4lunch.ui.main;

import android.content.Intent;
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
import com.davidvignon.go4lunch.databinding.MainActivityBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsViewModel;
import com.davidvignon.go4lunch.ui.map.MapFragment;
import com.davidvignon.go4lunch.ui.oauth.OAuthActivity;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsFragment;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnRestaurantClickedListener {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
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

        binding.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("DavidVgn", "task isSuccessful");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DavidVgn", "DocumentSnapshot data: " + document.getData());
                        String userPicturePath = document.get("picturePath").toString();

                        String fullName = firebaseUser.getDisplayName();
                        String email = firebaseUser.getProviderData().get(1).getEmail();

                        navUsername.setText(fullName);
                        navUseremail.setText(email);
                        Glide.with(MainActivity.this)
                            .load(userPicturePath)
                            .apply(RequestOptions.circleCropTransform())
                            .into(navUserImage);


                        Log.d("DavidVgn", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("DavidVgn", "No such document");
                    }
                } else {
                    Log.d("DavidVgn", "get failed with ", task.getException());
                }
            }
        });

        toolbar.setNavigationOnClickListener(view -> binding.mainDrawerLayout.open());

        binding.mainNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.nav_lunch):
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
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_FrameLayout_fragment_container, MapFragment.newInstance())
                .commit();
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
        startActivity(RestaurantDetailsViewModel.navigate(this, placeId));
    }

}