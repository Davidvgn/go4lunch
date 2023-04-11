package com.davidvignon.go4lunch.ui.main;


import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.AutocompleteRepository;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsItem;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.main.predictions.PredictionViewState;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    @NonNull
    private final PermissionRepository permissionRepository;
    @NonNull
    private final LocationRepository locationRepository;
    private final CurrentQueryRepository currentQueryRepository;

    private final MutableLiveData<Boolean> onMyLunchClickedMutableLiveData = new MutableLiveData<>();
    private final LiveData<MainViewState> mainViewStateLiveData;

    private final MediatorLiveData<EventWrapper<MainAction>> mainActionsMediatorLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<UserQueryAndLocation> userQueryAndLocationMediatorLiveData = new MediatorLiveData<>();
    private final LiveData<List<PredictionViewState>> predictionViewStateLiveData;

    private final MutableLiveData<String> queryMutableLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(
        @NonNull UserRepository userRepository,
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository,
        AutocompleteRepository autocompleteRepository,
        CurrentQueryRepository currentQueryRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;
        this.currentQueryRepository = currentQueryRepository;

        LiveData<String> placeIdLiveData = userRepository.getRestaurantPlaceIdLiveData();
        LiveData<User> userLiveData = userRepository.getUserLiveData();

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        mainViewStateLiveData = Transformations.map(userLiveData, user -> new MainViewState(
            user.getName(),
            user.getEmail(),
            user.getPicturePath()
        ));

        mainActionsMediatorLiveData.addSource(placeIdLiveData, placeId -> combineEvent(placeId, onMyLunchClickedMutableLiveData.getValue()));
        mainActionsMediatorLiveData.addSource(onMyLunchClickedMutableLiveData, onMyLunchClicked -> combineEvent(placeIdLiveData.getValue(), onMyLunchClicked));

        userQueryAndLocationMediatorLiveData.addSource(locationLiveData, location -> combine(location, queryMutableLiveData.getValue()));
        userQueryAndLocationMediatorLiveData.addSource(queryMutableLiveData, query -> combine(locationLiveData.getValue(), query));
        predictionViewStateLiveData = Transformations.map(
            Transformations.switchMap(
                userQueryAndLocationMediatorLiveData,
                userQueryAndLocation -> autocompleteRepository.getPredictionsResponse(
                    userQueryAndLocation.location.getLatitude(),
                    userQueryAndLocation.location.getLongitude(),
                    userQueryAndLocation.query
                )
            ),
            predictionsResponse -> {
                List<PredictionViewState> predictionViewStateList = new ArrayList<>();
                    for (PredictionsItem response : predictionsResponse.getPredictions()) {
                        predictionViewStateList.add(
                            new PredictionViewState(
                                response.getPlaceId(),
                                response.getDescription()
                            )
                        );
                    }
                    return predictionViewStateList;
            }
        );
    }

    @NonNull
    public LiveData<List<PredictionViewState>> getPredictionsViewStateLiveData() {
        return predictionViewStateLiveData;
    }

    private void combine(Location location, String query) {
        userQueryAndLocationMediatorLiveData.setValue(new UserQueryAndLocation(location, query));
    }

    private void combineEvent(@Nullable String placeId, @Nullable Boolean onMyLunchClicked) {

        if (onMyLunchClicked != null && onMyLunchClicked) {
            onMyLunchClickedMutableLiveData.setValue(false);
            if (placeId == null) {
                mainActionsMediatorLiveData.setValue(new EventWrapper<>(new MainAction.Toast(R.string.no_restaurant_selected)));
            } else {
                mainActionsMediatorLiveData.setValue(new EventWrapper<>(new MainAction.DetailNavigation(placeId)));
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (permissionRepository.isLocationPermissionGranted()) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

    public LiveData<MainViewState> getMainViewStateLiveData() {
        return mainViewStateLiveData;
    }

    public LiveData<EventWrapper<MainAction>> getMainActionLiveData() {
        return mainActionsMediatorLiveData;
    }

    public void onMyLunchClicked() {
        onMyLunchClickedMutableLiveData.setValue(true);
    }

    public void getSearchQueryText(String text) {
        queryMutableLiveData.setValue(text);
    }

    public void onSearchedRestaurantSelected(@Nullable String query) {
        currentQueryRepository.setCurrentRestaurantQuery(query);
    }

    private static class UserQueryAndLocation {
        private final Location location;
        private final String query;

        private UserQueryAndLocation(Location location, String query) {
            this.location = location;
            this.query = query;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserQueryAndLocation that = (UserQueryAndLocation) o;
            return Objects.equals(location, that.location) && Objects.equals(query, that.query);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, query);
        }

        @Override
        public String toString() {
            return "UserQueryAndLocation{" +
                "location=" + location +
                ", query='" + query + '\'' +
                '}';
        }
    }
}
