package com.davidvignon.go4lunch.ui.map;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final NearBySearchRepository nearBySearchRepository;

    @Inject
    public MapViewModel(@NonNull LocationRepository locationRepository, @NonNull NearBySearchRepository nearBySearchRepository) {
        this.locationRepository = locationRepository;
        this.nearBySearchRepository = nearBySearchRepository;
    }

    public LiveData<List<MapPoiViewState>> getMapPoiViewStateLiveData() {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationRepository.getLocationLiveData(),
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

    public SingleLiveEvent<CameraUpdate> getFocusOnUser() {
        SingleLiveEvent<CameraUpdate> mediatorLiveData = new SingleLiveEvent<>();
        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        mediatorLiveData.addSource(locationLiveData, location -> {
            if (location != null) {
                mediatorLiveData.removeSource(locationLiveData);
                mediatorLiveData.setValue(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 15
                    )
                );
            }
        });

        return mediatorLiveData;
    }
}

