package com.davidvignon.go4lunch.ui.restaurant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;

import android.location.Location;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.OpeningHoursResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.utils.DistanceCalculator;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantViewModel;
import com.davidvignon.go4lunch.ui.restaurants.RestaurantViewState;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsViewModelTest {

    private static final String DEFAULT_RESTAURANT_RESPONSE_NAME = "DEFAULT_RESTAURANT_RESPONSE_NAME";
    private static final String DEFAULT_RESTAURANT_RESPONSE_PLACE_ID = "DEFAULT_RESTAURANT_RESPONSE_PLACE_ID";
    private static final String DEFAULT_VICINITY = "DEFAULT_VICINITY";
    private static final String DEFAULT_PHOTO_REFERENCE = "DEFAULT_PHOTO_REFERENCE";
    private static final String DEFAULT_DISTANCE = "50m";
    private static final double DEFAULT_RATING = 3.4;

    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;
    private static final double DEFAULT_LONGITUDE_OFFSET = 46.0;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

    private final  DistanceCalculator distanceCalculator = Mockito.mock(DistanceCalculator.class);

    private RestaurantViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();

        locationMutableLiveData.setValue(location);

        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponse());

        Mockito.doReturn(50).when(distanceCalculator).distanceBetween(anyDouble(), anyDouble(), anyDouble(), anyDouble());

        viewModel = new RestaurantViewModel(locationRepository, nearBySearchRepository, distanceCalculator);
    }

    @Test
    public void initial_case() {
        // When
        List<RestaurantViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertEquals(getDefaultRestaurantViewState(), viewStates);
    }

    @Test
    public void if_all_or_one_element_is_null_it_returns_no_response() {
        // Given
        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponseWithElementsMissing());

        // When
        List<RestaurantViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());

        // Then
        assertTrue(viewStates.isEmpty());

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
            DEFAULT_RATING
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
        results.add(getDefaultRestaurantResponseWithNullPhoto(4));
        results.add(getDefaultRestaurantResponseWithNullVicinity(5));

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
            DEFAULT_RATING
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithEverythingNull() {
        return getDefaultRestaurant(
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
            DEFAULT_RATING
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullPhoto(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            null,
            DEFAULT_VICINITY + i,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING
        );
    }

    private RestaurantResponse getDefaultRestaurantResponseWithNullVicinity(int i) {
        return getDefaultRestaurant(
            DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
            DEFAULT_RESTAURANT_RESPONSE_NAME + i,
            getDefaultPhotos(i),
            null,
            getDefaultOpeningHoursResponse(i),
            DEFAULT_RATING
        );
    }

    private RestaurantResponse getDefaultRestaurant(
        @Nullable String placeId,
        @Nullable String name,
        @Nullable List<PhotosItemResponse> photosItemResponse,
        @Nullable String vicinity,
        @Nullable OpeningHoursResponse openingHoursResponse,
        @Nullable Double rating
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
            getGeometryResponse(0),
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
    private List<RestaurantViewState> getDefaultRestaurantViewState() {
        List<RestaurantViewState> result = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            result.add(
                new RestaurantViewState(
                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
                    DEFAULT_VICINITY + i,
                    DEFAULT_PHOTO_REFERENCE + i,
                    i == 1 ? R.string.open : R.string.closed,
                    2.04F,
                    DEFAULT_DISTANCE
                )
            );
        }

        return result;
    }
    // endregion
}
