package com.davidvignon.go4lunch.data.google_places;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PlaceDetailsRepositoryTest {

    private static final String PLACE_ID = "PLACE_ID";
    private static final String DEFAULT_KEY = BuildConfig.NEARBY_API_KEY;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PlacesApi placeDetailsApi = Mockito.mock(PlacesApi.class);

    private final Call call = Mockito.mock(Call.class);
    private final ArgumentCaptor<Callback> callBack = ArgumentCaptor.forClass(Callback.class);

    private PlaceDetailsRepository placeDetailsRepository;

    @Before
    public void setUp(){
        placeDetailsRepository = new PlaceDetailsRepository(placeDetailsApi);
        Mockito.doNothing().when(call).enqueue(any());
        Mockito.doReturn(call).when(placeDetailsApi).getDetailsResponse(
            PLACE_ID,
            DEFAULT_KEY
        );
    }

    @Test
    public void nominal_case(){
        // Given
        Response retrofitResponse = Mockito.mock(Response.class);
        DetailsResponse expectedResponse = Mockito.mock(DetailsResponse.class);
        Mockito.doReturn(expectedResponse).when(retrofitResponse).body();

        // When
        LiveData<DetailsResponse> liveData = placeDetailsRepository.getDetailsResponseLiveData(PLACE_ID);
        Mockito.verify(placeDetailsApi).getDetailsResponse(PLACE_ID, DEFAULT_KEY);
        Mockito.verify(call).enqueue(callBack.capture());
        callBack.getValue().onResponse(call, retrofitResponse);

        DetailsResponse result = LiveDataTestUtils.getValueForTesting(liveData);

        // Then
        assertEquals(expectedResponse, result);
    }
}
