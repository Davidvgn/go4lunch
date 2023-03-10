package com.davidvignon.go4lunch.ui.main;

import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MainViewModelTest {

    private final String DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID";
    private final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private final String DEFAULT_PICTURE_PATH = "DEFAULT_PICTURE_PATH";
    private final String DEFAULT_EMAIL = "DEFAULT_EMAIL";
    private final String DEFAULT_SELECTED_RESTAURANT = "DEFAULT_SELECTED_RESTAURANT";
    private final String DEFAULT_LIKED_RESTAURANT_NAME = "DEFAULT_LIKED_RESTAURANT_NAME";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);

    MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    private MainViewModel viewModel;

    @Before
    public void setUp(){

        placeIdMutableLiveData.setValue(DEFAULT_PLACE_ID);
        userMutableLiveData.setValue(getUser());

        Mockito.doReturn(placeIdMutableLiveData).when(userRepository).getRestaurantPlaceIdLiveData();
        Mockito.doReturn(userMutableLiveData).when(userRepository).getUserLiveData();
        Mockito.doReturn(true).when(permissionRepository).isLocationPermissionGranted();
        Mockito.doNothing().when(locationRepository).startLocationRequest();
        Mockito.doNothing().when(locationRepository).stopLocationRequest();

        viewModel = new MainViewModel(userRepository, permissionRepository, locationRepository);
    }

    @Test
    public void checks_if_permission_is_granted_it_starts_location_request (){
        // When
        viewModel.refresh();

        // Then
        Mockito.verify(locationRepository).startLocationRequest();
        Mockito.verify(locationRepository, Mockito.never()).stopLocationRequest();

    }

    @Test
    public void checks_if_permission_is_not_granted_it_starts_location_request (){
        // Given
        Mockito.doReturn(false).when(permissionRepository).isLocationPermissionGranted();

        // When
        viewModel.refresh();

        // Then
        Mockito.verify(locationRepository, Mockito.never()).startLocationRequest();
        Mockito.verify(locationRepository).stopLocationRequest();
    }


    private User getUser(){
        return new User(
            "42",
            DEFAULT_USER_NAME,
            DEFAULT_PICTURE_PATH,
            DEFAULT_EMAIL,
            getRestaurantLikedByUserList(),
            DEFAULT_SELECTED_RESTAURANT
        );
    }

    private List<String> getRestaurantLikedByUserList(){
        List<String> restaurantLikedList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            restaurantLikedList.add(
                DEFAULT_LIKED_RESTAURANT_NAME + i
            );
        }
        return restaurantLikedList;
    }
}
