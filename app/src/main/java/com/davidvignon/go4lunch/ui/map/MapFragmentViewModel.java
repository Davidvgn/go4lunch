package com.davidvignon.go4lunch.ui.map;


import android.content.Context;
import android.hardware.Camera;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.RetrofitService;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@HiltViewModel
public class MapFragmentViewModel extends ViewModel {

    @NonNull
    LocationRepository locationRepository;

    @Inject
    public MapFragmentViewModel(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public LiveData<List<MapPoiViewState>> getMapPoiViewStateLiveData() {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(getLocationLiveData(locationRepository.getLocationLiveData()), new Function<Location, LiveData<NearbySearchResponse>>() {
            @Override
            public LiveData<NearbySearchResponse> apply(Location location) {
                return getNearbySearchResponse(location.getLatitude(), location.getLongitude());            }
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
        return mapPois;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public LiveData<CameraUpdate> getFocusOnUser(){
        LiveData<CameraUpdate> cameraFocusLiveData = Transformations.map(getLocationLiveData(locationRepository.getLocationLiveData()), new Function<Location, CameraUpdate>() {
            @Override
            public CameraUpdate apply(Location location) {
                return CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 15
                );
            }
        });
       return cameraFocusLiveData;
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

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    private LiveData<Location> getLocationLiveData(LiveData<Location> location) {
        location = locationRepository.getLocationLiveData();
        return location;

    }


//    public CameraUpdate getFocusOnUserLocation() {
//
//        LiveData<CameraUpdate> cameraFocusLiveData = Transformations.map(getLocationLiveData(locationRepository.getLocationLiveData()), new Function<Location, CameraUpdate>() {
////
////            @Override
////            public CameraUpdate apply(Location location) {
////                return CameraUpdateFactory.newLatLngZoom(
////                    new LatLng(location.getLatitude(), location.getLongitude()),
////                    15
////
////                );
////            }
////        });
//    }
}


//
////        LiveData<CameraUpdate> cameraFocusLiveData = Transformations.map(locationLiveData, new Function<Location, CameraUpdate>() {
////
////            @Override
////            public CameraUpdate apply(Location location) {
////                return CameraUpdateFactory.newLatLngZoom(
////                    new LatLng(location.getLatitude(), location.getLongitude()),
////                    15
////
////                );
////            }
////        });
//        Log.i("Dvgn", "mapPoi: " + mapPois);
//        return mapPois;
//    }




////    @SuppressLint("MissingPermission")
////    public void refresh() {
////        boolean getLocationPermission = permissionRepository.hasLocationPermission();
////        hasPermission.setValue(getLocationPermission);
////
////        if (getLocationPermission) {
//////                requestPermi.launch(new String[]{
//////            Manifest.permission.ACCESS_FINE_LOCATION,
//////            Manifest.permission.ACCESS_COARSE_LOCATION
//////        });
////        } else {
////        }
////    }

//}
