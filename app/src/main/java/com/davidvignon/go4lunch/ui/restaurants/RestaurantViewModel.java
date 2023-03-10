
package com.davidvignon.go4lunch.ui.restaurants;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.utils.DistanceCalculator;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.facebook.internal.Mutable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantViewModel extends ViewModel {

    @NonNull
    private final NearBySearchRepository nearBySearchRepository;
    private final WorkmateRepository workmateRepository;

    @NonNull
    private final DistanceCalculator distanceCalculator;

    private final LiveData<List<RestaurantViewState>> restaurantViewState;
    private final MutableLiveData<Integer> workmatesGoingThereMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();


    @Inject
    public RestaurantViewModel(
        @NonNull LocationRepository locationRepository,
        @NonNull NearBySearchRepository nearBySearchRepository,
        @NonNull WorkmateRepository workmateRepository,
        @NonNull DistanceCalculator distanceCalculator
    ) {

        this.nearBySearchRepository = nearBySearchRepository;
        this.workmateRepository = workmateRepository;
        this.distanceCalculator = distanceCalculator;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        restaurantViewState = bindViewState(locationLiveData);
    }

    @NonNull
    public LiveData<List<RestaurantViewState>> getRestaurantViewStateLiveData() {
        return restaurantViewState;
    }

    public LiveData<Integer> getNumberOfWorkmatesLiveData() {
        return workmatesGoingThereMutableLiveData;
    }

    public LiveData<String> getPlaceId() {
        return placeIdMutableLiveData;
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
                        && result.getPhotos().get(0) != null
                        && result.getPhotos().get(0).getPhotoReference() != null
                        && result.getOpeningHours() != null
                        && result.getRating() != null
                        && result.getGeometry() != null
                        && result.getGeometry().getLocation() != null
                        && result.getGeometry().getLocation().getLat() != null
                        && result.getGeometry().getLocation().getLng() != null
                    ) {

                        placeIdMutableLiveData.setValue(result.getPlaceId());

                        final int openOrClosed;
                        if (result.getOpeningHours().isOpenNow()) {
                            openOrClosed = R.string.open;
                        } else {
                            openOrClosed = R.string.closed;
                        }

                        Double initialRating = result.getRating();

                        Location userLocation = locationLiveData.getValue();

                        final Integer distanceText;

                        if (userLocation != null) {
                            distanceText = (distanceCalculator.distanceBetween(
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng(),
                                userLocation.getLatitude(),
                                userLocation.getLongitude()
                            ));

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
                                distanceText.toString(),
                                4 //todo david travailler là dessus fonction dans workmateRepo
                            )
                        );

                    }
                }
            }
            return viewStates;
        });
    }
}
