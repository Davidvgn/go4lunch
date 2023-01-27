package com.davidvignon.go4lunch.ui.main;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsViewModel;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final PermissionRepository permissionRepository;
    @NonNull
    private final LocationRepository locationRepository;

    private final LiveData<String> restaurantIdLiveData;
    private final SingleLiveEvent<String> showToastSingleLiveEvent = new SingleLiveEvent<>();


    @Inject
    public MainViewModel(@NonNull UserRepository userRepository, @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository) {
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;

        restaurantIdLiveData = Transformations.map(userRepository.getRestaurantPlaceId(), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input;
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (permissionRepository.isLocationPermissionGranted()) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

    public LiveData<String> getSelectedRestaurant() {
        return restaurantIdLiveData;
    }

}
