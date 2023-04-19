package com.davidvignon.go4lunch.ui.main;

import static org.junit.Assert.assertEquals;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.AutocompleteRepository;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsItem;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsResponse;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.main.predictions.PredictionViewState;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MainViewModelTest {

    private static final String DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_PICTURE_PATH = "DEFAULT_PICTURE_PATH";
    private static final String DEFAULT_EMAIL = "DEFAULT_EMAIL";
    private static final String DEFAULT_SELECTED_RESTAURANT = "DEFAULT_SELECTED_RESTAURANT";
    private static final String DEFAULT_LIKED_RESTAURANT_NAME = "DEFAULT_LIKED_RESTAURANT_NAME";

    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
    private static final String DEFAULT_QUERY = "DEFAULT_QUERY";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final AutocompleteRepository autocompleteRepository = Mockito.mock(AutocompleteRepository.class);
    private final CurrentQueryRepository currentQueryRepository = Mockito.mock(CurrentQueryRepository.class);

    private final MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<PredictionsResponse> predictionsResponseMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> currentRestaurantQueryLiveData = new MutableLiveData<>();


    private MainViewModel viewModel;

    @Before
    public void setUp() {

        placeIdMutableLiveData.setValue(DEFAULT_PLACE_ID);
        userMutableLiveData.setValue(getUser());
        predictionsResponseMutableLiveData.setValue(getPredictionsResponse());
        currentRestaurantQueryLiveData.setValue(DEFAULT_QUERY);

        Mockito.doReturn(predictionsResponseMutableLiveData).when(autocompleteRepository).getPredictionsResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_DESCRIPTION);

        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        Mockito.doReturn(currentRestaurantQueryLiveData).when(currentQueryRepository).getCurrentRestaurantQuery();

        locationMutableLiveData.setValue(location);

        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Mockito.doReturn(placeIdMutableLiveData).when(userRepository).getRestaurantPlaceIdLiveData();
        Mockito.doReturn(userMutableLiveData).when(userRepository).getUserLiveData();
        Mockito.doReturn(true).when(permissionRepository).isLocationPermissionGranted();
        Mockito.doNothing().when(locationRepository).startLocationRequest();
        Mockito.doNothing().when(locationRepository).stopLocationRequest();

        viewModel = new MainViewModel(userRepository, permissionRepository, locationRepository, autocompleteRepository, currentQueryRepository);

    }


    @Test
    public void nominal_case_mainViewState() {
        // When
        MainViewState mainViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMainViewStateLiveData());

        // Then
        assertEquals(getDefaultUserViewState(), mainViewState);
    }

    @Test
    public void query_gives_predictions() {
        // Given
        viewModel.getSearchQueryText(DEFAULT_DESCRIPTION);

        // When
        List<PredictionViewState> predictionViewState = LiveDataTestUtils.getValueForTesting(viewModel.getPredictionsViewStateLiveData());

        // Then
        assertEquals((getDefaultPredictionsViewState()), predictionViewState);
    }

    @Test
    public void right_query_goes_to_currentQueryRepository() {
        viewModel.onSearchedRestaurantSelected(DEFAULT_QUERY);

        String queryMutableLiveDataValue = LiveDataTestUtils.getValueForTesting(currentQueryRepository.getCurrentRestaurantQuery());
        assertEquals(DEFAULT_QUERY, queryMutableLiveDataValue);

    }

    @Test
    public void checks_if_permission_is_granted_it_starts_location_request() {
        // When
        viewModel.refresh();

        // Then
        Mockito.verify(locationRepository).startLocationRequest();
        Mockito.verify(locationRepository, Mockito.never()).stopLocationRequest();

    }

    @Test
    public void checks_if_permission_is_not_granted_it_starts_location_request() {
        // Given
        Mockito.doReturn(false).when(permissionRepository).isLocationPermissionGranted();

        // When
        viewModel.refresh();

        // Then
        Mockito.verify(locationRepository, Mockito.never()).startLocationRequest();
        Mockito.verify(locationRepository).stopLocationRequest();
    }

    private User getUser() {
        return new User(
            "42",
            DEFAULT_USER_NAME,
            DEFAULT_PICTURE_PATH,
            DEFAULT_EMAIL,
            getRestaurantLikedByUserList(),
            DEFAULT_SELECTED_RESTAURANT
        );
    }


    private List<String> getRestaurantLikedByUserList() {
        List<String> restaurantLikedList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            restaurantLikedList.add(
                DEFAULT_LIKED_RESTAURANT_NAME + i
            );
        }
        return restaurantLikedList;
    }

    private MainViewState getDefaultUserViewState() {
        MainViewState user = new MainViewState(
            DEFAULT_USER_NAME,
            DEFAULT_EMAIL,
            DEFAULT_PICTURE_PATH
        );
        return user;
    }

    private List<PredictionViewState> getDefaultPredictionsViewState() {
        List<PredictionViewState> predictionViewState = new ArrayList<>();

        predictionViewState.add(new PredictionViewState(
            DEFAULT_PLACE_ID,
            DEFAULT_DESCRIPTION));
        return predictionViewState;
    }

    private PredictionsResponse getPredictionsResponse() {
        List<PredictionsItem> predictionsItemList = new ArrayList<>();
        predictionsItemList.add(getDefaultPredictionItem(
            DEFAULT_DESCRIPTION,
            DEFAULT_PLACE_ID));

        return new PredictionsResponse(
            predictionsItemList,
            null
        );
    }

    private PredictionsItem getDefaultPredictionItem(String description, String placeId) {
        PredictionsItem predictionsItem = Mockito.mock(PredictionsItem.class);
        Mockito.doReturn(description).when(predictionsItem).getDescription();
        Mockito.doReturn(placeId).when(predictionsItem).getPlaceId();

        return predictionsItem;
    }
}

