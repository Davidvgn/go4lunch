package com.davidvignon.go4lunch.ui.map;


import android.location.Location;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.CurrentSearchRepository;
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
    private final CurrentSearchRepository currentSearchRepository;
    private final LiveData<List<MapPoiViewState>> mapPoiViewStatesLiveData;
    private final SingleLiveEvent<LatLng> cameraUpdateSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public MapViewModel(
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository,
        @NonNull NearBySearchRepository nearBySearchRepository,
        CurrentSearchRepository currentSearchRepository
    ) {
        this.nearBySearchRepository = nearBySearchRepository;
        this.permissionRepository = permissionRepository;
        this.currentSearchRepository = currentSearchRepository;


        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        bindCameraUpdate(locationLiveData);
        mapPoiViewStatesLiveData = getViewStateLiveData(locationLiveData);
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
    private LiveData<List<MapPoiViewState>> getViewStateLiveData(LiveData<Location> locationLiveData) {

        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationLiveData,
            location -> nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude())
        );

        LiveData<String> currentOnSearchedLiveData = currentSearchRepository.getOnSearchRestaurantSelected();

        MediatorLiveData<List<MapPoiViewState>> mediatorLiveData = new MediatorLiveData<>();

        mediatorLiveData.addSource(nearbySearchResponseLiveData, new Observer<NearbySearchResponse>() {
            @Override
            public void onChanged(NearbySearchResponse nearbySearchResponse) {
                combine(mediatorLiveData, nearbySearchResponse, currentOnSearchedLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(currentOnSearchedLiveData, new Observer<String>() {
            @Override
            public void onChanged(String query) {
                combine(mediatorLiveData, nearbySearchResponseLiveData.getValue(), query);

            }
        });

        return mediatorLiveData;
    }

    private void combine(
        @NonNull MediatorLiveData<List<MapPoiViewState>> mediatorLiveData,
        @Nullable NearbySearchResponse response,
        @Nullable String searchedQuery
    ) {
        if (response == null) {
            return;
        }
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

                    if (searchedQuery != null) {
                        if (compareNameAndQuery(result.getName(), searchedQuery)) {
                            viewStates.add(
                                new MapPoiViewState(
                                    result.getPlaceId(),
                                    result.getName(),
                                    result.getGeometry().getLocation().getLat(),
                                    result.getGeometry().getLocation().getLng(),
                                    0.0F
                                ));
                        } else {
                            viewStates.add(
                                new MapPoiViewState(
                                    result.getPlaceId(),
                                    result.getName(),
                                    result.getGeometry().getLocation().getLat(),
                                    result.getGeometry().getLocation().getLng(),
                                    60.0F
                                ));
                        }
                    } else {
                        viewStates.add(
                            new MapPoiViewState(
                                result.getPlaceId(),
                                result.getName(),
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng(),
                                0.0F
                            )
                        );
                    }
                }
            }
        }
        mediatorLiveData.setValue(viewStates);
    }

    private void bindCameraUpdate(LiveData<Location> locationLiveData) {
        cameraUpdateSingleLiveEvent.addSource(locationLiveData, location -> {
            if (location != null) {
                cameraUpdateSingleLiveEvent.removeSource(locationLiveData);
                cameraUpdateSingleLiveEvent.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    private boolean compareNameAndQuery(String restaurantName, String query) {
        restaurantName = restaurantName.toLowerCase();
        query = query.toLowerCase();
        int i = 0;
        for (int j = 0; j < restaurantName.length() && i < query.length(); j++) {
            if (restaurantName.charAt(j) == query.charAt(i)) {
                i++;
            }
        }
        return i == query.length();
    }
}