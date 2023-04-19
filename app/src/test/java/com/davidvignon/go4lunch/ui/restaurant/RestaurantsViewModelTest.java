package com.davidvignon.go4lunch.ui.restaurant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;

import android.location.Location;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.OpeningHoursResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.utils.DistanceCalculator;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsViewModel;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantsViewState;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantsViewModelTest {

    private static final String DEFAULT_RESTAURANT_RESPONSE_NAME = "DEFAULT_RESTAURANT_RESPONSE_NAME";
    private static final String DEFAULT_RESTAURANT_RESPONSE_PLACE_ID = "DEFAULT_RESTAURANT_RESPONSE_PLACE_ID";
    private static final String DEFAULT_VICINITY = "DEFAULT_VICINITY";
    private static final String DEFAULT_PHOTO_REFERENCE = "DEFAULT_PHOTO_REFERENCE";
    private static final String DEFAULT_DISTANCE = "50";
    private static final double DEFAULT_RATING = 3.4;
    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;
    private static final double DEFAULT_LONGITUDE_OFFSET = 5.0;
    private static final int DEFAULT_WORKMATES_GOING_THERE = 0;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
    private final WorkmateRepository workmateRepository = Mockito.mock(WorkmateRepository.class);
    private final CurrentQueryRepository currentQueryRepository = Mockito.mock(CurrentQueryRepository.class);

    private final DistanceCalculator distanceCalculator = Mockito.mock(DistanceCalculator.class);
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Integer>> placeIdUserCountMapMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData <String> currentRestaurantQueryLiveData = new MutableLiveData<>();


    private RestaurantsViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(50).when(distanceCalculator).distanceBetween(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        Mockito.doReturn(currentRestaurantQueryLiveData).when(currentQueryRepository).getCurrentRestaurantQuery();

        locationMutableLiveData.setValue(location);

        placeIdUserCountMapMutableLiveData.setValue(new HashMap<>() {{
            put(DEFAULT_RESTAURANT_RESPONSE_PLACE_ID, DEFAULT_WORKMATES_GOING_THERE);
        }});

        Mockito.doReturn(placeIdUserCountMapMutableLiveData).when(workmateRepository).getPlaceIdUserCountMapLiveData();

        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponse());


        viewModel = new RestaurantsViewModel(locationRepository, nearBySearchRepository, workmateRepository, currentQueryRepository,distanceCalculator);
    }

    @Test
    public void initial_case() {
        // When
        List<RestaurantsViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertEquals(getDefaultRestaurantViewState(), viewStates);
    }

    @Test
    public void if_all_or_one_element_is_null_it_returns_no_response() {
        // Given
        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponseWithElementsMissing());

        // When
        List<RestaurantsViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertTrue(viewStates.isEmpty());

    }

    @Test
    public void if_no_query_all_restaurants_are_displayed(){
        // Given
        currentRestaurantQueryLiveData.setValue(null);

        // When
        List<RestaurantsViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertEquals(getDefaultRestaurantViewState(), viewStates);
    }

    @Test
    public void if_no_match_no_restaurant_are_displayed(){
        // Given
        currentRestaurantQueryLiveData.setValue("query");

        // When
        List<RestaurantsViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertEquals(0, viewStates.size());
    }

    @Test
    public void if_query_matches_only_match_are_displayed(){
        // Given
        currentRestaurantQueryLiveData.setValue("D0");

        // When
        List<RestaurantsViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertEquals(1, viewStates.size());
    }

    // region IN
    private NearbySearchResponse getDefaultNearbySearchResponse() {
        List<RestaurantResponse> results = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            results.add(getDefaultRestaurantResponse(i));
        }

        return new NearbySearchResponse(
            null,
            null,
            results,
            null
        );
    }

    private RestaurantResponse getDefaultRestaurantResponse(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            DEFAULT_VICINITY + i,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private OpeningHoursResponse getDefaultOpeningHoursResponse(int i) {
        return new OpeningHoursResponse(i == 1);
    }

    private List<PhotosItemResponse> getDefaultPhotos(int i) {
        List<PhotosItemResponse> photosItemResponses = new ArrayList<>();

        photosItemResponses.add(new PhotosItemResponse(DEFAULT_PHOTO_REFERENCE + i));
        return photosItemResponses;
    }

    private NearbySearchResponse getDefaultNearbySearchResponseWithElementsMissing() {
        List<RestaurantResponse> results = new ArrayList<>();

        results.add(getDefaultRestaurantResponseWithEverythingNull());
        results.add(getDefaultRestaurantResponseWithNullName(1));
        results.add(getDefaultRestaurantResponseWithNullPlaceId(2));
        results.add(getDefaultRestaurantResponseWithNullGeometry(3));
        results.add(getDefaultRestaurantResponseWithNullPhoto(4));
        results.add(getDefaultRestaurantResponseWithNullVicinity(5));
        results.add(getDefaultRestaurantResponseWithNullOpeningHours(6));
        results.add(getDefaultRestaurantResponseWithNullRating(7));

        return new NearbySearchResponse(
            null,
            null,
            results,
            null
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullPlaceId(int i) {
        return getDefaultRestaurant(
            null,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            DEFAULT_VICINITY + i,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithEverythingNull() {
        return getDefaultRestaurant(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullName(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            null,
            getDefaultPhotos(i),
            DEFAULT_VICINITY + i,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullGeometry(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            DEFAULT_VICINITY + i,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING,
            null,
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullPhoto(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            null,
            DEFAULT_VICINITY + i,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullVicinity(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            null,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullOpeningHours(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            DEFAULT_VICINITY,
            null,
            DEFAULT_RATING,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullRating(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            DEFAULT_VICINITY,
            getDefaultOpeningHoursResponse(i),
            null,
            getGeometryResponse(i),
            "" + DEFAULT_WORKMATES_GOING_THERE

        );
    }

    private RestaurantResponse getDefaultRestaurant(
        @Nullable String placeId,
        @Nullable String name,
        @Nullable List<PhotosItemResponse> photosItemResponse,
        @Nullable String vicinity,
        @Nullable OpeningHoursResponse openingHoursResponse,
        @Nullable Double rating,
        @Nullable GeometryResponse geometryResponse,
        @Nullable String workmatesGoingThere
    ) {
        return new RestaurantResponse(
            null,
            null,
            null,
            rating,
            null,
            photosItemResponse,
            null,
            null,
            null,
            null,
            name,
            openingHoursResponse,
            geometryResponse,
            null,
            vicinity,
            null,
            placeId,
            null
        );
    }


    private GeometryResponse getGeometryResponse(int i) {
        return new GeometryResponse(
            null,
            new LocationResponse(
                DEFAULT_LONGITUDE_OFFSET + i,
                (double) i
            )
        );
    }

    //endregion IN

    // region OUT
    private List<RestaurantsViewState> getDefaultRestaurantViewState() {
        List<RestaurantsViewState> result = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            result.add(
                new RestaurantsViewState(
                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
                    DEFAULT_VICINITY + i,
                    DEFAULT_PHOTO_REFERENCE + i,
                    i == 1 ? R.string.open : R.string.closed,
                    2.04F,
                    DEFAULT_DISTANCE,
                    "" + DEFAULT_WORKMATES_GOING_THERE
                )
            );
        }

        return result;
    }

    private List<RestaurantsViewState> getDefaultRestaurantViewStateWithNoDistance() {
        List<RestaurantsViewState> result = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            result.add(
                new RestaurantsViewState(
                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
                    DEFAULT_VICINITY + i,
                    DEFAULT_PHOTO_REFERENCE + i,
                    i == 1 ? R.string.open : R.string.closed,
                    2.04F,
                    null,
                    "" + DEFAULT_WORKMATES_GOING_THERE
                )
            );
        }

        return result;
    }
    // endregion OUT
}
