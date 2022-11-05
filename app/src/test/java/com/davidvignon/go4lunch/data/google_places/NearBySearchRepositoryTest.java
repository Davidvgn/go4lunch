//package com.davidvignon.go4lunch.data.google_places;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.mockito.ArgumentMatchers.any;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//import androidx.lifecycle.MutableLiveData;
//
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
//import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(MockitoJUnitRunner.class)
//public class NearBySearchRepositoryTest {
//
//    private static final double DEFAULT_LATITUDE = 45.757830302;
//    private static final double DEFAULT_LONGITUDE = 4.823496706;
//    private static final double DEFAULT_LONGITUDE_OFFSET = 46.0;
//    private static final String DEFAULT_RESTAURANT_RESPONSE_NAME = "DEFAULT_RESTAURANT_RESPONSE_NAME";
//    private static final String DEFAULT_RESTAURANT_RESPONSE_PLACE_ID = "DEFAULT_RESTAURANT_RESPONSE_PLACE_ID";
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
//
//    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
//
//    @Before
//    public void setUp() {
//        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
//        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponse());
//    }
//
//    @Test
//    public void test(){
//        // When
//        NearbySearchResponse nearbySearchResponse = LiveDataTestUtils.getValueForTesting(nearBySearchRepository.getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE));
//
//        // Then
//        assertEquals(nearbySearchResponse, getDefaultNearbySearchResponse());
//    }
//
//
//    private NearbySearchResponse getDefaultNearbySearchResponse() {
//        List<RestaurantResponse> results = new ArrayList<>();
//         results.add(
//                new RestaurantResponse(
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    DEFAULT_RESTAURANT_RESPONSE_NAME ,
//                    null,
//                    getGeometryResponse(1),
//                    null,
//                    null,
//                    null,
//                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID,
//                    null
//                )
//            );
//
//        return new NearbySearchResponse(
//            null,
//            null,
//            results,
//            null
//        );
//    }
//
//    private GeometryResponse getGeometryResponse(int i) {
//        return new GeometryResponse(
//            null,
//            new LocationResponse(
//                DEFAULT_LONGITUDE_OFFSET + i,
//                (double) i
//            )
//        );
//    }
//
//    }
//
