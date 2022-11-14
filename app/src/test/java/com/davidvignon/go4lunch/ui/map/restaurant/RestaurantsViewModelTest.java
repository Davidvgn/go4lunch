//package com.davidvignon.go4lunch.ui.map.restaurant;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import android.location.Location;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//import androidx.lifecycle.MutableLiveData;
//
//import com.davidvignon.go4lunch.data.google_places.LocationRepository;
//import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
//import com.davidvignon.go4lunch.ui.restaurants.RestaurantViewModel;
//import com.davidvignon.go4lunch.ui.restaurants.RestaurantViewState;
//import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RestaurantsViewModelTest {
//
//    private static final String DEFAULT_RESTAURANT_RESPONSE_NAME = "DEFAULT_RESTAURANT_RESPONSE_NAME";
//    private static final String DEFAULT_RESTAURANT_RESPONSE_PLACE_ID = "DEFAULT_RESTAURANT_RESPONSE_PLACE_ID";
//    private static final String DEFAULT_VICINITY = "DEFAULT_VICINITY";
//    private static final String DEFAULT_PHOTO = "DEFAULT_PHOTO";
//    private static final String DEFAULT_PHOTO_REFERENCE = "DEFAULT_PHOTO_REFERENCE";
//
//    private static final double DEFAULT_LATITUDE = 45.757830302;
//    private static final double DEFAULT_LONGITUDE = 4.823496706;
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
//    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
//
//    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
//    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
//
//    private RestaurantViewModel viewModel;
//
//    @Before
//    public void setUp() {
//        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
//        Location location = Mockito.mock(Location.class);
//        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
//        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
//        locationMutableLiveData.setValue(location);
//
//        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
//        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponse());
//
//        viewModel = new RestaurantViewModel(locationRepository, nearBySearchRepository);
//    }
//
//
//    @Test
//    public void initial_case() {
//        List<RestaurantViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());
//        assertEquals(getDefaultRestaurantViewState(), viewStates);
//    }
//
//    @Test
//    public void if_all_or_one_element_is_null_it_returns_no_response() {
//        // Given
//        nearbySearchResponseMutableLiveData.setValue(getNearbySearchResponseWithElementMissing());
//
//        // When
//        List<RestaurantViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantViewStateLiveData());
//
//        // Then
//        assertTrue(viewStates.isEmpty());
//
//    }
//
//    // region IN
//    private NearbySearchResponse getDefaultNearbySearchResponse() {
//        List<RestaurantResponse> results = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            results.add(
//                new RestaurantResponse(
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    getPhoto(i),
//                    null,
//                    null,
//                    null,
//                    null,
//                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
//                    null,
//                    null,
//                    null,
//                    DEFAULT_VICINITY + i,
//                    null,
//                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
//                    null
//                )
//            );
//        }
//
//        return new NearbySearchResponse(
//            null,
//            null,
//            results,
//            null
//        );
//    }
//
//    private List<RestaurantViewState> getDefaultRestaurantViewState() {
//        List<RestaurantViewState> result = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            result.add(
//                new RestaurantViewState(
//                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
//                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
//                    DEFAULT_VICINITY + i,
//                    DEFAULT_PHOTO + i
//                )
//            );
//        }
//
//        return result;
//    }
//
//    private List<PhotosItemResponse> getPhoto(int i) {
//        List<PhotosItemResponse> photosItemResponses = new ArrayList<>();
//
//        photosItemResponses.add(
//            new PhotosItemResponse(
//                DEFAULT_PHOTO_REFERENCE)
//        );
//        return photosItemResponses;
//    }
//
//    private NearbySearchResponse getNearbySearchResponseWithElementMissing() {
//        List<RestaurantResponse> results = new ArrayList<>();
//
//        //all elements are null
//        results.add(
//            new RestaurantResponse(
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//            )
//        );
//
//        //name is null
//        results.add(
//            new RestaurantResponse(
//                null,
//                null,
//                null,
//                null,
//                null,
//                getPhoto(1),
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                DEFAULT_VICINITY + 1,
//                null,
//                DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + 1,
//                null
//            )
//        );
//
//        //placeId is null
//        results.add(
//            new RestaurantResponse(
//                null,
//                null,
//                null,
//                null,
//                null,
//                getPhoto(3),
//                null,
//                null,
//                null,
//                null,
//                DEFAULT_RESTAURANT_RESPONSE_NAME + 3,
//                null,
//                null,
//                null,
//                DEFAULT_VICINITY + 3,
//                null,
//                null,
//                null
//            )
//        );
//
//        //photo is null
//        results.add(
//            new RestaurantResponse(
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                DEFAULT_RESTAURANT_RESPONSE_NAME + 4,
//                null,
//                null,
//                null,
//                DEFAULT_VICINITY + 4,
//                null,
//                DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + 4,
//                null
//            )
//        );
//
//
//        //vicinaty is null
//        results.add(
//            new RestaurantResponse(
//                null,
//                null,
//                null,
//                null,
//                null,
//                getPhoto(5),
//                null,
//                null,
//                null,
//                null,
//                DEFAULT_RESTAURANT_RESPONSE_NAME + 5,
//                null,
//                null,
//                null,
//                null,
//                null,
//                DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + 5,
//                null
//            )
//        );
//
//
//        return new NearbySearchResponse(
//            null,
//            null,
//            results,
//            null
//        );
//    }
//
//    //endregion IN
//}
