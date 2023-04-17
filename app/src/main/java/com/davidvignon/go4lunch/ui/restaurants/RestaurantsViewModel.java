
package com.davidvignon.go4lunch.ui.restaurants;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.utils.DistanceCalculator;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {
    @NonNull
    private final DistanceCalculator distanceCalculator;
    private final MediatorLiveData<List<RestaurantsViewState>> mediatorLiveData = new MediatorLiveData<>();

    @Inject
    public RestaurantsViewModel(
        @NonNull LocationRepository locationRepository,
        @NonNull NearBySearchRepository nearBySearchRepository,
        @NonNull WorkmateRepository workmateRepository,
        @NonNull CurrentQueryRepository currentQueryRepository,
        @NonNull DistanceCalculator distanceCalculator
    ) {
        this.distanceCalculator = distanceCalculator;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<NearbySearchResponse> nearbySearchResponsesLiveData = Transformations.switchMap(
            locationLiveData,
            location -> nearBySearchRepository.getNearbySearchResponse(location.getLatitude(), location.getLongitude())
        );
        LiveData<String> currentOnSearchedLiveData = currentQueryRepository.getCurrentRestaurantQuery();
        LiveData<Map<String, Integer>> placeIdUserCountMapLiveData = workmateRepository.getPlaceIdUserCountMapLiveData();


        mediatorLiveData.addSource(nearbySearchResponsesLiveData, nearbySearchResponse -> combine(locationLiveData, nearbySearchResponse, placeIdUserCountMapLiveData.getValue(), currentOnSearchedLiveData.getValue()));

        mediatorLiveData.addSource(placeIdUserCountMapLiveData, placeIdUserCountMap -> combine(locationLiveData, nearbySearchResponsesLiveData.getValue(), placeIdUserCountMap, currentOnSearchedLiveData.getValue()));

        mediatorLiveData.addSource(currentOnSearchedLiveData, query -> combine(locationLiveData, nearbySearchResponsesLiveData.getValue(), placeIdUserCountMapLiveData.getValue(), query));
    }

    @NonNull
    public LiveData<List<RestaurantsViewState>> getRestaurantViewStateLiveData() {
        return mediatorLiveData;
    }

    private void combine(
        @NonNull LiveData<Location> locationLiveData,
        @Nullable NearbySearchResponse response,
        @Nullable Map<String, Integer> placeIdUserCountMap,
        @Nullable String searchedQuery
    ) {
        if (response == null) {
            return;
        }

        List<RestaurantsViewState> viewStates = new ArrayList<>();

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
                    if (Boolean.TRUE.equals(result.getOpeningHours().isOpenNow())) {
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

                    String usersGoingToThisRestaurantText = "0";

                    if (placeIdUserCountMap != null) {
                        Integer usersGoingToThisRestaurant = placeIdUserCountMap.get(result.getPlaceId());

                        if (usersGoingToThisRestaurant != null) {
                            usersGoingToThisRestaurantText = usersGoingToThisRestaurant.toString();
                        }
                    }

                    if (searchedQuery == null || isRestaurantNamePartialMatchForQuery(result.getName(), searchedQuery)
                    ) {
                        PhotosItemResponse photosItem = result.getPhotos().get(0);
                        if (photosItem != null && photosItem.getPhotoReference() != null) {

                            viewStates.add(
                                new RestaurantsViewState(
                                    result.getPlaceId(),
                                    result.getName(),
                                    result.getVicinity(),
                                    photosItem.getPhotoReference(),
                                    openOrClosed,
                                    (float) (initialRating * 3 / 5),
                                    distanceText != null ? distanceText.toString() : "",
                                    usersGoingToThisRestaurantText
                                )
                            );
                        }
                    }
                }
            }
        }
        mediatorLiveData.setValue(viewStates);
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
