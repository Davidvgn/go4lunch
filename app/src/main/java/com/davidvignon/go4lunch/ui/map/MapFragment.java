package com.davidvignon.go4lunch.ui.map;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;

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
import androidx.lifecycle.Transformations;

import com.davidvignon.go4lunch.data.google_places.RetrofitService;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends SupportMapFragment {

    //        LatLng latLng = (new LatLng(45.7282, 4.8307));
    FusedLocationProviderClient fusedLocationProviderClient;
    //        LatLng latLng = (new LatLng(45.5180288, 6.1407232));
    LatLng latLng;
    Location currentLocation;

    double lat;
    double lon;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

//        fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
//            @NonNull
//            @Override
//            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
//                return null;
//            }
//
//            @Override
//            public boolean isCancellationRequested() {
//                return false;
//            }
//        }).addOnSuccessListener(location -> {
//            currentLocation = location;
//            lat = currentLocation.getLatitude();
//            lon = currentLocation.getLongitude();
//            Log.d("Dvgn", "onSuccessListener() called with: location = [" + location + "]");
//
//        });

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

                fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }

                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                }).addOnSuccessListener(location -> {
                    currentLocation = location;
                    lat = currentLocation.getLatitude();
                    lon = currentLocation.getLongitude();
                    Log.d("Dvgn", "onSuccessListener() called with: location = [" + location + "]");
                    latLng = new LatLng(lat, lon);
                    Log.d("Dvgn", "onMapReady() called with: googleMap = [" + lat + "]");

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    googleMap.addMarker(
                        new MarkerOptions()
                            .position(latLng)
                            .title("Ninka Lyon Gerland")
                            .alpha(0.8f)
                    );

                    PlacesApi placesApi = RetrofitService.getPlacesApi();

                    placesApi.getNearbySearchResponse(location.getLongitude() + "," + location.getLatitude(),
                        "1500",
                        "restaurant",
                        "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8").enqueue(new Callback<NearbySearchResponse>() {

                        @Override
                        public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                            NearbySearchResponse nearbySearchResponse = response.body();
//
                            for (RestaurantResponse result : nearbySearchResponse.getResults()) {
                                Marker responseMarker = googleMap.addMarker(
                                    new MarkerOptions()
                                        .position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()))
                                        .title(result.getName())
                                        .alpha(0.8f)
                                        .icon(BitmapDescriptorFactory.defaultMarker(HUE_ROSE))
                                );
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {

                        }
                    });
                });

            }
        });
    }
}
