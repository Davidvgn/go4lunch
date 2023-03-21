package com.davidvignon.go4lunch.ui.main;


import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.AutocompleteRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsItem;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsResponse;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.map.MapPoiViewState;
import com.davidvignon.go4lunch.ui.predictions.PredictionViewState;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    @NonNull
    private final PermissionRepository permissionRepository;
    @NonNull
    private final LocationRepository locationRepository;
    private final AutocompleteRepository autocompleteRepository;

    private final MutableLiveData<Boolean> onMyLunchClickedMutableLiveData = new MutableLiveData<>();
    private final LiveData<MainViewState> mainViewStateLiveData;

    private final MediatorLiveData<EventWrapper<MainAction>> mainActionsMediatorLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<PredictionViewState>> predictionViewStateMediatorLiveData = new MediatorLiveData<>();


    private MutableLiveData<String> queryMutableLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(
        @NonNull UserRepository userRepository,
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository,
        AutocompleteRepository autocompleteRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;
        this.autocompleteRepository = autocompleteRepository;

        LiveData<String> placeIdLiveData = userRepository.getRestaurantPlaceIdLiveData();
        LiveData<User> userLiveData = userRepository.getUserLiveData();

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        LiveData<String> queryLiveData = queryMutableLiveData;

            predictionViewStateMediatorLiveData.addSource(locationLiveData, new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    combine(location, queryLiveData.getValue());
                }
            });

            predictionViewStateMediatorLiveData.addSource(queryLiveData, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    combine(locationLiveData.getValue(), s);
                }
            });

        mainViewStateLiveData = Transformations.map(userLiveData, new Function<User, MainViewState>() {
            @Override
            public MainViewState apply(User user) {
                return new MainViewState(
                    user.getName(),
                    user.getEmail(),
                    user.getPicturePath()
                );
            }
        });

        mainActionsMediatorLiveData.addSource(placeIdLiveData, placeId -> combineEvent(placeId, onMyLunchClickedMutableLiveData.getValue()));

        mainActionsMediatorLiveData.addSource(onMyLunchClickedMutableLiveData, onMyLunchClicked -> combineEvent(placeIdLiveData.getValue(), onMyLunchClicked));

    }

    @NonNull
    public LiveData<List<PredictionViewState>> getPredictionsViewStateLiveData() {
        return predictionViewStateMediatorLiveData;
    }

    private void combine(Location location, String query) {

        LiveData<List<PredictionViewState>> predictionsResponseLiveData = Transformations.map(
            autocompleteRepository.getPredictionsResponse(location.getLatitude(), location.getLongitude(), query),
            new Function<PredictionsResponse, List<PredictionViewState>>() {
                @Override
                public List<PredictionViewState> apply(PredictionsResponse input) {
                    List<PredictionViewState> predictionViewStateList = new ArrayList<>();
                    for (PredictionsItem response : input.getPredictions()) {
                        predictionViewStateList.add(
                            new PredictionViewState(
                                response.getPlaceId(),
                                response.getDescription()));
                    }
                    return predictionViewStateList;
                }
            });
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
}
