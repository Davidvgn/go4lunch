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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

        requestPermissions(
            new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            },
            0
        );

        getMapAsync(googleMap -> {
            viewModel.getMapPoiViewStateLiveData().observe(getViewLifecycleOwner(), mapPoiViewStates -> {

                for (MapPoiViewState result : mapPoiViewStates) {
                    googleMap.addMarker(
                        new MarkerOptions()
                            .position(new LatLng(result.getLatitude(), result.getLongitude()))
                            .title(result.getTitle())
                            .alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(@NonNull Marker marker) {
                            for (MapPoiViewState result : mapPoiViewStates) {
                                int compareLat = Double.compare(marker.getPosition().longitude, result.getLongitude());
                                int compareLong = Double.compare(marker.getPosition().latitude, result.getLatitude());

                                if (compareLat == 0 && compareLong == 0) {
                                    if (getContext() != null) {
                                        startActivity(RestaurantDetailsViewModel.navigate(getContext(), result.getPlaceId()));
                                    }
                                    break;

                                }
                            }
                        }
                    });
                }


                //todo NINO : est ce que la bonne méthode ou je reste sur la partie commentée
                viewModel.isLocationGrantedLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @SuppressLint("MissingPermission") //checked in PermissionRepository
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        googleMap.setMyLocationEnabled(aBoolean);
                        googleMap.setPadding(0, 2100, 0, 0);
                    }
                });

//                if (ActivityCompat.checkSelfPermission(
//                    getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    googleMap.setMyLocationEnabled(viewModel.onLocationButtonClicked());
//                }
            });

            viewModel.getFocusOnUser().observe(getViewLifecycleOwner(), latLng -> googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)));
        });
    }
}