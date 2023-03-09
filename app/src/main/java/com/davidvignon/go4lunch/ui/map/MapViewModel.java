package com.davidvignon.go4lunch.ui.map;


import android.location.Location;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    @NonNull
    private final NearBySearchRepository nearBySearchRepository;

    @NonNull
    private final PermissionRepository permissionRepository;

    private final LiveData<List<MapPoiViewState>> mapPoiViewStatesLiveData;
    private final SingleLiveEvent<LatLng> cameraUpdateSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public MapViewModel(
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository,
        @NonNull NearBySearchRepository nearBySearchRepository
    ) {
        this.nearBySearchRepository = nearBySearchRepository;
        this.permissionRepository = permissionRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        bindCameraUpdate(locationLiveData);
        mapPoiViewStatesLiveData = bindViewState(locationLiveData);
    }

    @NonNull
    public LiveData<List<MapPoiViewState>> getMapPoiViewStateLiveData() {
        return mapPoiViewStatesLiveData;
    }

    @NonNull
    public SingleLiveEvent<LatLng> getFocusOnUser() {
        return cameraUpdateSingleLiveEvent;
    }


    public LiveData<Boolean> isLocationGrantedLiveData() {
        return permissionRepository.isUserLocationGrantedLiveData();
    }

        @NonNull
    private LiveData<List<MapPoiViewState>> bindViewState(LiveData<Location> locationLiveData) {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationLiveData,
            location -> nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude())
        );

        return Transformations.map(nearbySearchResponseLiveData, response -> {
            List<MapPoiViewState> viewStates = new ArrayList<>();

            if (response.getResults() != null) {
                for (RestaurantResponse result : response.getResults()) {
                    if (
                        result != null
                            && result.getName() != null
                            && result.getPlaceId() != null
                            && result.getGeometry() != null
                            && result.getGeometry().getLocation() != null
                            && result.getGeometry().getLocation().getLat() != null
                            && result.getGeometry().getLocation().getLng() != null
                    ) {
                        viewStates.add(
                            new MapPoiViewState(
                                result.getPlaceId(),
                                result.getName(),
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng()
                            )
                        );
                    }
                }
            }
            return viewStates;
        });
    }

    private void bindCameraUpdate(LiveData<Location> locationLiveData) {
        cameraUpdateSingleLiveEvent.addSource(locationLiveData, location -> {
            if (location != null) {
                cameraUpdateSingleLiveEvent.removeSource(locationLiveData);
                cameraUpdateSingleLiveEvent.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
    }
}