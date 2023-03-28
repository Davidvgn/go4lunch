package com.davidvignon.go4lunch.ui.map;


import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
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
    private final PermissionRepository permissionRepository;

    private final MediatorLiveData<List<MapPoiViewState>> mapPoiViewStatesLiveData = new MediatorLiveData<>();
    private final SingleLiveEvent<LatLng> cameraUpdateSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public MapViewModel(
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository,
        @NonNull NearBySearchRepository nearBySearchRepository,
        CurrentQueryRepository currentQueryRepository
    ) {
        this.permissionRepository = permissionRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        cameraUpdateSingleLiveEvent.addSource(locationLiveData, location -> {
            if (location != null) {
                cameraUpdateSingleLiveEvent.removeSource(locationLiveData);
                cameraUpdateSingleLiveEvent.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });

        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationLiveData,
            location -> nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude())
        );

        LiveData<String> currentRestaurantQueryLiveData = currentQueryRepository.getCurrentRestaurantQuery();

        mapPoiViewStatesLiveData.addSource(nearbySearchResponseLiveData, nearbySearchResponse ->
            combine(nearbySearchResponse, currentRestaurantQueryLiveData.getValue())
        );

        mapPoiViewStatesLiveData.addSource(currentRestaurantQueryLiveData, query ->
            combine(nearbySearchResponseLiveData.getValue(), query)
        );
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

    private void combine(
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

                    float hue = getHue(searchedQuery, result);

                    viewStates.add(
                        new MapPoiViewState(
                            result.getPlaceId(),
                            result.getName(),
                            result.getGeometry().getLocation().getLat(),
                            result.getGeometry().getLocation().getLng(),
                            hue
                        )
                    );
                }
            }
        }

        mapPoiViewStatesLiveData.setValue(viewStates);
    }

    private float getHue(@Nullable String searchedQuery, @NonNull RestaurantResponse result) {
        if (searchedQuery == null || result.getName() == null || !isRestaurantNamePartialMatchForQuery(result.getName(), searchedQuery)) {
            return 0.0F;
        } else {
            return 60.0F;
        }
    }

    private boolean isRestaurantNamePartialMatchForQuery(final @NonNull String restaurantName, final @NonNull String query) {
        String restaurantNameLowercase = restaurantName.toLowerCase();
        String queryLowercase = query.toLowerCase();
        int i = 0;
        for (int j = 0; j < restaurantNameLowercase.length() && i < queryLowercase.length(); j++) {
            if (restaurantNameLowercase.charAt(j) == queryLowercase.charAt(i)) {
                i++;
            }
        }
        return i == queryLowercase.length();
    }
}