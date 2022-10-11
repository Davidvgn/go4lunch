package com.davidvignon.go4lunch.ui.map;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.GetNearByRepository;
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
    private final LocationRepository locationRepository;
    @NonNull
    private final GetNearByRepository getNearbyRepository;

    @Inject
    public MapFragmentViewModel(@NonNull LocationRepository locationRepository, @NonNull GetNearByRepository getNearbyRepository) {
        this.locationRepository = locationRepository;
        this.getNearbyRepository = getNearbyRepository;
    }


    public LiveData<List<MapPoiViewState>> getMapPoiViewStateLiveData() {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationRepository.getLocationLiveData(),
            location -> getNearbyRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude())
        );

        return Transformations.map(nearbySearchResponseLiveData, response -> {
            List<MapPoiViewState> viewStates = new ArrayList<>();

            if (response.getResults() != null) {
                for (RestaurantResponse result : response.getResults()) {
                    if (
                        result != null
                            && result.getPlaceId() != null
                            && result.getGeometry() != null
                            && result.getGeometry().getLocation() != null
                            && result.getGeometry().getLocation().getLat() != null
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

    public LiveData<CameraUpdate> getFocusOnUser() {
        MediatorLiveData<CameraUpdate> mediatorLiveData = new MediatorLiveData<>();
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

