package com.davidvignon.go4lunch.ui.map;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
    private final LocationRepository locationRepository;

    @Inject
    public MapFragmentViewModel(@NonNull LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LiveData<List<MapPoiViewState>> getMapPoiViewStateLiveData() {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationRepository.getLocationLiveData(),
            location -> getNearbySearchResponse(location.getLatitude(), location.getLongitude())
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

    // TODO DAVID To repository
    private LiveData<NearbySearchResponse> getNearbySearchResponse(double latitude, double longitude) {
        MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
        PlacesApi placesApi = RetrofitService.getPlacesApi(); // TODO DAVID Hilt @Provide

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

