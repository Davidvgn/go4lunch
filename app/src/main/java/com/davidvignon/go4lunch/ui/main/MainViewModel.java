package com.davidvignon.go4lunch.ui.main;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    @NonNull
    private final PermissionRepository permissionRepository;
    @NonNull
    private final LocationRepository locationRepository;

    private final MutableLiveData<Boolean> onMyLunchClickedMutableLiveData = new MutableLiveData<>();
    private final LiveData<MainViewState> mainViewStateLiveData;

    private final MediatorLiveData<EventWrapper<MainAction>> mainActionsMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public MainViewModel(
        @NonNull UserRepository userRepository,
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;

        LiveData<String> placeIdLiveData = userRepository.getRestaurantPlaceId();
        LiveData<User> userLiveData = userRepository.getUserLiveData();

        mainViewStateLiveData = Transformations.map(userLiveData, user -> new MainViewState(
            user.getName(),
            user.getEmail(),
            user.getPicturePath()
        ));

        mainActionsMediatorLiveData.addSource(placeIdLiveData, placeId -> combineEvent(placeId, onMyLunchClickedMutableLiveData.getValue()));

        mainActionsMediatorLiveData.addSource(onMyLunchClickedMutableLiveData, onMyLunchClicked -> combineEvent(placeIdLiveData.getValue(), onMyLunchClicked));
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

}
