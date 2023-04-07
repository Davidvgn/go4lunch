package com.davidvignon.go4lunch.ui.main;


import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.AutocompleteRepository;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsResponse;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;

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
    private final String INPUT = "INPUT";

    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final AutocompleteRepository autocompleteRepository = Mockito.mock(AutocompleteRepository.class);
    private final CurrentQueryRepository currentQueryRepository = Mockito.mock(CurrentQueryRepository.class);

    private final MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<PredictionsResponse> predictionsResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData <String> currentRestaurantQueryLiveData = new MutableLiveData<>();

    private MainViewModel viewModel;

    @Before
    public void setUp(){

        placeIdMutableLiveData.setValue(DEFAULT_PLACE_ID);
        userMutableLiveData.setValue(getUser());

        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        Mockito.doReturn(currentRestaurantQueryLiveData).when(currentQueryRepository).getCurrentRestaurantQuery();

        locationMutableLiveData.setValue(location);

        //input devrait etre changeable suivant le test pas en final static


        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Mockito.doReturn(predictionsResponseMutableLiveData).when(autocompleteRepository).getPredictionsResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, INPUT);
        Mockito.doReturn(placeIdMutableLiveData).when(userRepository).getRestaurantPlaceIdLiveData();
        Mockito.doReturn(userMutableLiveData).when(userRepository).getUserLiveData();
        Mockito.doReturn(true).when(permissionRepository).isLocationPermissionGranted();
        Mockito.doNothing().when(locationRepository).startLocationRequest();
        Mockito.doNothing().when(locationRepository).stopLocationRequest();

        viewModel = new MainViewModel(userRepository, permissionRepository, locationRepository,autocompleteRepository, currentQueryRepository);
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

    @Test
    public void test(){

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
