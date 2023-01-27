package com.davidvignon.go4lunch.ui.main;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
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

    private final MutableLiveData<Void> onMyLunchClickedMutableLiveData = new MutableLiveData<>();

    private final LiveData<EventWrapper<MainAction>> mainActionsLiveData;

    @Inject
    public MainViewModel(
        @NonNull UserRepository userRepository,
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;

        mainActionsLiveData = Transformations.switchMap(
            userRepository.getRestaurantPlaceId(),
            placeId -> Transformations.map(
                onMyLunchClickedMutableLiveData,
                ping -> {
                    if (placeId == null) {
                        return new EventWrapper<>(new MainAction.Toast(R.string.no_restaurant_selected));
                    } else {
                        return new EventWrapper<>(new MainAction.DetailNavigation(placeId));
                    }
                }
            )
        );
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (permissionRepository.isLocationPermissionGranted()) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

    public LiveData<EventWrapper<MainAction>> getMainActionLiveData() {
        return mainActionsLiveData;
    }

    public void onMyLunchClicked() {
        onMyLunchClickedMutableLiveData.setValue(null);
    }

}
