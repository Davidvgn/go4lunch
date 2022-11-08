package com.davidvignon.go4lunch.ui.restaurants;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantViewModel extends ViewModel {

    @NonNull
    private final NearBySearchRepository nearBySearchRepository;

    private final LiveData<List<RestaurantViewState>> restaurantViewState;
    private final MediatorLiveData<List<RestaurantViewState>> mediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> hour = new MutableLiveData<>();


    @Inject
    public RestaurantViewModel(@NonNull LocationRepository locationRepository, @NonNull NearBySearchRepository nearBySearchRepository) {
        this.nearBySearchRepository = nearBySearchRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        restaurantViewState = bindViewState(locationLiveData);

        mediatorLiveData.addSource(hour, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }

    @NonNull
    public LiveData<List<RestaurantViewState>> getRestaurantViewStateLiveData() {
        return restaurantViewState;
    }

    @NonNull
    private LiveData<List<RestaurantViewState>> bindViewState(LiveData<Location> locationLiveData) {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationLiveData,
            location -> nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude())
        );

        return Transformations.map(nearbySearchResponseLiveData, response -> {
            List<RestaurantViewState> viewStates = new ArrayList<>();

            if (response.getResults() != null) {
                for (RestaurantResponse result : response.getResults()) {
                    if (result != null
                        && result.getPlaceId() != null
                        && result.getName() != null
                        && result.getVicinity() != null
                        && result.getPhotos() != null
                        && result.getOpeningHours() != null
                        && result.getPhotos() != null
                        && result.getPhotos().get(0) != null
                        && result.getPhotos().get(0).getPhotoReference() != null
                    ) {
                        viewStates.add(
                            new RestaurantViewState(
                                result.getPlaceId(),
                                result.getName(),
                                result.getVicinity(),
                                result.getOpeningHours(),
                                result.getPhotos().get(0).getPhotoReference()
                            )
                        );
                    }
                }
            }
            return viewStates;
        });
    }
}
