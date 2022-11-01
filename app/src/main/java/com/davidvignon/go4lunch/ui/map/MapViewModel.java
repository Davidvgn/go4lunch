package com.davidvignon.go4lunch.ui.map;


import android.location.Location;


import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;

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

    SingleLiveEvent<LatLng> mediatorLiveData = new SingleLiveEvent<>();
    MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();


    @Inject
    public MapViewModel(@NonNull LocationRepository locationRepository, @NonNull NearBySearchRepository nearBySearchRepository
    ) {
        this.locationRepository = locationRepository;
        this.nearBySearchRepository = nearBySearchRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        locationMutableLiveData.setValue(locationLiveData.getValue());

        mediatorLiveData.addSource(locationLiveData, location -> {
            if (location != null) {
                mediatorLiveData.removeSource(locationLiveData);
                mediatorLiveData.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    public LiveData<List<MapPoiViewState>> getMapPoiViewStateLiveData() {

//        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
//            locationRepository.getLocationLiveData(),
//            new Function<Location, LiveData<NearbySearchResponse>>() {
//                @Override
//                public LiveData<NearbySearchResponse> apply(Location location) {
//                    return nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude());
//                }
//            }
//        );
        //todo Nino est ce la solution ? :
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(locationMutableLiveData,
            location -> nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude()));

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

    public SingleLiveEvent<LatLng> getFocusOnUser() {
        return mediatorLiveData;
    }
}