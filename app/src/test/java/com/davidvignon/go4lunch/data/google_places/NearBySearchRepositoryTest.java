//todo david voir si on garde le placesClient dans nearby repo.
//package com.davidvignon.go4lunch.data.google_places;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//import androidx.lifecycle.LiveData;
//
//import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
//import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//@SuppressWarnings({"rawtypes", "unchecked"})
//public class NearBySearchRepositoryTest {
//
//    private static final double DEFAULT_LATITUDE = 45.757830302;
//    private static final double DEFAULT_LONGITUDE = 4.823496706;
//    private static final String DEFAULT_RADIUS = "1500";
//    private static final String DEFAULT_TYPE = "restaurant";
//    private static final String DEFAULT_KEY = "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8";
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    private final PlacesApi placesApi = Mockito.mock(PlacesApi.class);
//
//    private final Call call = Mockito.mock(Call.class);
//    private final ArgumentCaptor<Callback> callback = ArgumentCaptor.forClass(Callback.class);
//
//    private NearBySearchRepository nearBySearchRepository;
//
//    @Before
//    public void setUp() {
//        nearBySearchRepository = new NearBySearchRepository(placesApi);
//        Mockito.doNothing().when(call).enqueue(any());
//        Mockito.doReturn(call).when(placesApi).getNearbySearchResponse(
//            DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE,
//            DEFAULT_RADIUS,
//            DEFAULT_TYPE,
//            DEFAULT_KEY
//        );
//    }
//
//    @Test
//    public void nominal_case() {
//        // Given
//        Response retrofitResponse = Mockito.mock(Response.class);
//        NearbySearchResponse expectedResponse = Mockito.mock(NearbySearchResponse.class);
//        Mockito.doReturn(expectedResponse).when(retrofitResponse).body();
//
//        // When
//        LiveData<NearbySearchResponse> liveData = nearBySearchRepository.getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
//        Mockito.verify(placesApi).getNearbySearchResponse(
//            DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE,
//            DEFAULT_RADIUS,
//            DEFAULT_TYPE,
//            DEFAULT_KEY
//        );
//        Mockito.verify(call).enqueue(callback.capture());
//        callback.getValue().onResponse(call, retrofitResponse);
//
//        NearbySearchResponse result = LiveDataTestUtils.getValueForTesting(liveData);
//
//        // Then
//        assertEquals(expectedResponse, result);
//    }
//}
//
