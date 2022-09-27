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
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.RetrofitService;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends SupportMapFragment {

    FusedLocationProviderClient fusedLocationProviderClient;

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
                LiveData<Location> locationLiveData = getLocationLiveData();
                LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(locationLiveData, new Function<Location, LiveData<NearbySearchResponse>>() {
                    @Override
                    public LiveData<NearbySearchResponse> apply(Location location) {
                        return getNearbySearchResponse(location.getLatitude(), location.getLongitude());
                    }
                });
                LiveData<List<MapPoiViewState>> mapPois = Transformations.map(nearbySearchResponseLiveData, new Function<NearbySearchResponse, List<MapPoiViewState>>() {
                    @Override
                    public List<MapPoiViewState> apply(NearbySearchResponse response) {
                        Log.d("Dvgn", "Transformations.map called with: response = [" + response + "]");
                        List<MapPoiViewState> viewStates = new ArrayList<>();
                        viewStates.add(new MapPoiViewState(
                            "aezaeaze",
                            "Ninkazi Gerland",
                            45.7281729,
                            4.8307208
                        ));

                        for (RestaurantResponse result : response.getResults()) {
                            // TODO DAVID
                        }

                        return viewStates;
                    }
                });

                mapPois.observe(getViewLifecycleOwner(), new Observer<List<MapPoiViewState>>() {
                    @Override
                    public void onChanged(List<MapPoiViewState> pois) {
                        Log.d("Dvgn", "mapPois.onChanged() called with: pois = [" + pois + "]");
                        for (MapPoiViewState mapPoiViewState : pois) {

                            googleMap.addMarker(
                                new MarkerOptions()
                                    .position(new LatLng(mapPoiViewState.getLatitude(), mapPoiViewState.getLongitude()))
                                    .title(mapPoiViewState.getTitle())
                                    .alpha(0.8f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_ROSE))
                            );
                        }
                    }
                });

                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    private LiveData<Location> getLocationLiveData() {
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
        Log.d("Dvgn", "getLocationLiveData() called");

        fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {
            Log.d("Dvgn", "getLocationLiveData().addOnSuccessListener() called with location = " + location);
            locationMutableLiveData.setValue(location);
        });

        return locationMutableLiveData;
    }


    private LiveData<NearbySearchResponse> getNearbySearchResponse(double latitude, double longitude) {
        MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
        Log.d("Dvgn", "getNearbySearchResponse() called with: latitude = [" + latitude + "], longitude = [" + longitude + "]");

        PlacesApi placesApi = RetrofitService.getPlacesApi();

        placesApi.getNearbySearchResponse(
            latitude + "," + longitude,
            "1500",
            "restaurant",
            "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8"
        ).enqueue(new Callback<NearbySearchResponse>() {

            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                Log.d("Dvgn", "getNearbySearchResponse().onResponse() called with: result number = " + response.body().getResults().size());
                nearbySearchResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {

            }
        });

        return nearbySearchResponseMutableLiveData;
    }
}
