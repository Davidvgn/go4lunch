package com.davidvignon.go4lunch.ui.map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.RetrofitService;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MapFragment extends SupportMapFragment {

    @Inject
    LocationRepository locationRepository;

    MapFragmentViewModel viewModel;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MapFragmentViewModel.class);
//        locationRepository.getPermission();

        ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                    .RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted.
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted.
                    } else {
                        // No location access granted.
                    }
                }
            );
        locationPermissionRequest.launch(new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        });

        getMapAsync(new OnMapReadyCallback() {
            @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                viewModel.getMapPoiViewStateLiveData().observe(getViewLifecycleOwner(), new Observer<List<MapPoiViewState>>() {
                    @Override
                    public void onChanged(List<MapPoiViewState> mapPoiViewStates) {

                        for (MapPoiViewState result : mapPoiViewStates) {
                            googleMap.addMarker(
                                new MarkerOptions()
                                    .position(new LatLng(result.getLatitude(), result.getLongitude()))
                                    .title(result.getTitle())
                                    .alpha(0.8f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                    }
                });

                viewModel.getFocusOnUser().observe(getViewLifecycleOwner(), new Observer<CameraUpdate>() {
                    @Override
                    public void onChanged(CameraUpdate cameraUpdate) {
                        googleMap.animateCamera(cameraUpdate);


                    }
                });

            }
        });
    }
}