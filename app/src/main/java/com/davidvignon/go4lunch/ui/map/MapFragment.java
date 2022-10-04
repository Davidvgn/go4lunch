package com.davidvignon.go4lunch.ui.map;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.RetrofitService;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationRepository.getPermission();

        getMapAsync(new OnMapReadyCallback() {
            @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) { //todo david à dynamiser dans une liveData
                LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

                LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(locationLiveData, new Function<Location, LiveData<NearbySearchResponse>>() {
                    @Override
                    public LiveData<NearbySearchResponse> apply(Location location) {
                        return getNearbySearchResponse(location.getLatitude(), location.getLongitude());
                    }
                });

                LiveData<List<MapPoiViewState>> mapPois = Transformations.map(nearbySearchResponseLiveData, new Function<NearbySearchResponse, List<MapPoiViewState>>() {
                    @Override
                    public List<MapPoiViewState> apply(NearbySearchResponse response) {
                        List<MapPoiViewState> viewStates = new ArrayList<>();

                        for (RestaurantResponse result : response.getResults()) {
                            viewStates.add(new MapPoiViewState(
                                result.getPlaceId(),
                                result.getName(),
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng()
                            ));
                        }
                        return viewStates;
                    }
                });

                mapPois.observe(getViewLifecycleOwner(), new Observer<List<MapPoiViewState>>() {
                    @Override
                    public void onChanged(List<MapPoiViewState> pois) {
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

                LiveData<CameraUpdate> cameraFocusLiveData = Transformations.map(locationLiveData, new Function<Location, CameraUpdate>() {
                    @Override
                    public CameraUpdate apply(Location location) {
                        return CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()),
                            15
                        );
                    }
                });

                cameraFocusLiveData.observe(getViewLifecycleOwner(), new Observer<CameraUpdate>() {
                    @Override
                    public void onChanged(CameraUpdate cameraUpdate) {
                        googleMap.animateCamera(cameraUpdate);
                    }
                });
            }
        });
    }

    private LiveData<NearbySearchResponse> getNearbySearchResponse(double latitude, double longitude) {
        MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

        PlacesApi placesApi = RetrofitService.getPlacesApi();

        placesApi.getNearbySearchResponse(
            latitude + "," + longitude,
            "1500",
            "restaurant",
            "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8"
        ).enqueue(new Callback<NearbySearchResponse>() {

            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                nearbySearchResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {

            }
        });

        return nearbySearchResponseMutableLiveData;
    }
}
