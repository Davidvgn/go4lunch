package com.davidvignon.go4lunch.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.view.fragments.listView.ListViewFragment;
import com.davidvignon.go4lunch.view.fragments.mapView.MapViewFragment;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import android.os.Bundle;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationBarView bottomNavigationView = findViewById(R.id.main_menu);
//        bottomNavigationView.setOnItemReselectedListener(navListener);

}

//    private NavigationBarView.OnItemReselectedListener navListener =
//        item -> {
//            Fragment selectedFragment = null;
//
//            switch (item.getItemId()) {
//                case R.id.bottom_nav_map:
//                    selectedFragment = new ();
//                    break;
//                case R.id.bottom_nav_list:
//                    selectedFragment = new ();
//                    break;
//                case R.id.bottom_nav_workmates:
//                    selectedFragment = new ();
//                    break;
//            }

//        };
}