package com.davidvignon.go4lunch.ui.map;

import android.Manifest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.ui.details.RestaurantDetailsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends SupportMapFragment {

    @NonNull
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapViewModel viewModel = new ViewModelProvider(this).get(MapViewModel.class);

        //noinspection deprecation
        requestPermissions(
            new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            },
            0
        );

        getMapAsync(googleMap -> {
            viewModel.getMapPoiViewStateLiveData().observe(getViewLifecycleOwner(), mapPoiViewStates -> {
                googleMap.clear();
                for (MapPoiViewState result : mapPoiViewStates) {
                    googleMap.addMarker(
                        new MarkerOptions()
                            .position(new LatLng(result.getLatitude(), result.getLongitude()))
                            .title(result.getTitle())
                            .alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(result.getHue())));
                    googleMap.setOnInfoWindowClickListener(marker -> startActivity(RestaurantDetailsViewModel.navigate(requireContext(), result.getPlaceId())));
                }
            });

            //checked in PermissionRepository
            //noinspection Convert2Lambda,Anonymous2MethodRef
            viewModel.isLocationGrantedLiveData().observe(MapFragment.this.getViewLifecycleOwner(), new Observer<>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onChanged(Boolean aBoolean) {
                    googleMap.setMyLocationEnabled(aBoolean);
                }
            });

            viewModel.getFocusOnUser().observe(getViewLifecycleOwner(), latLng -> googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)));
        });
    }
}