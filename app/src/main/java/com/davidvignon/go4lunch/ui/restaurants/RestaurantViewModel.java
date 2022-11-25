package com.davidvignon.go4lunch.ui.restaurants;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.utils.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantViewModel extends ViewModel {

    @NonNull
    private final NearBySearchRepository nearBySearchRepository;

    @NonNull
    private final DistanceCalculator distanceCalculator;

    private final LiveData<List<RestaurantViewState>> restaurantViewState;

    @Inject
    public RestaurantViewModel(
        @NonNull LocationRepository locationRepository,
        @NonNull NearBySearchRepository nearBySearchRepository,
        @NonNull DistanceCalculator distanceCalculator
    ) {
        this.nearBySearchRepository = nearBySearchRepository;
        this.distanceCalculator = distanceCalculator;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        restaurantViewState = bindViewState(locationLiveData);
    }

    @NonNull
    public LiveData<List<RestaurantViewState>> getRestaurantViewStateLiveData() {
        return restaurantViewState;
    }

    @NonNull
    private LiveData<List<RestaurantViewState>> bindViewState(LiveData<Location> locationLiveData) {
        LiveData<NearbySearchResponse> nearbySearchResponseLiveData = Transformations.switchMap(
            locationLiveData,
            new Function<Location, LiveData<NearbySearchResponse>>() {
                @Override
                public LiveData<NearbySearchResponse> apply(Location location) {
                    return nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude());
                }
            }
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
                        && result.getPhotos().get(0) != null
                        && result.getPhotos().get(0).getPhotoReference() != null
                        && result.getOpeningHours() != null
                        && result.getRating() != null
                        && result.getGeometry() != null
                        && result.getGeometry().getLocation() != null
                        && result.getGeometry().getLocation().getLat() != null
                        && result.getGeometry().getLocation().getLng() != null
                    ) {
                        final int openOrClosed;
                        if (result.getOpeningHours().isOpenNow()) {
                            openOrClosed = R.string.open;
                        } else {
                            openOrClosed = R.string.closed;
                        }

                        Double initialRating = result.getRating();

                        Location userLocation = locationLiveData.getValue();

                        final String distanceText;

                        if (userLocation != null) {
                            int distanceInt = distanceCalculator.distanceBetween(
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng(),
                                userLocation.getLatitude(),
                                userLocation.getLongitude()
                            );

                            distanceText = distanceInt + "m"; // TODO David à extraire dans une resource string
                        } else {
                            distanceText = null;
                        }

                        //noinspection ConstantConditions
                        viewStates.add(
                            new RestaurantViewState(
                                result.getPlaceId(),
                                result.getName(),
                                result.getVicinity(),
                                result.getPhotos().get(0).getPhotoReference(),
                                openOrClosed,
                                (float) (initialRating * 3 / 5),
                                distanceText
                            )
                        );

                    }
                }
            }
            return viewStates;
        });
    }


}