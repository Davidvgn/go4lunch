package com.davidvignon.go4lunch.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsResponse;
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
public class AutocompleteRepositoryTest {

    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;
    private static final String DEFAULT_INPUT = "DEFAULT_INPUT";
    private static final String DEFAULT_RADIUS = "1500";
    private static final String DEFAULT_TYPE = "restaurant";
    private static final String DEFAULT_KEY = BuildConfig.AUTOCOMPLETE_API_KEY;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PlacesApi placesApi = Mockito.mock(PlacesApi.class);

    private final Call call = Mockito.mock(Call.class);
    private final ArgumentCaptor<Callback> callback = ArgumentCaptor.forClass(Callback.class);
    private AutocompleteRepository autocompleteRepository;

    @Before
    public void setUp(){
        autocompleteRepository = new AutocompleteRepository(placesApi);
        Mockito.doNothing().when(call).enqueue(any());
        Mockito.doReturn(call).when(placesApi).getPredictionsResponse(
            DEFAULT_INPUT,
            DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE,
            DEFAULT_RADIUS,
            DEFAULT_TYPE,
            DEFAULT_KEY
        );
    }

    @Test
    public void nominal_case() {
        // Given
        Response retrofitResponse = Mockito.mock(Response.class);
        PredictionsResponse expectedResponse = Mockito.mock(PredictionsResponse.class);
        Mockito.doReturn(expectedResponse).when(retrofitResponse).body();

        // When
        LiveData<PredictionsResponse> liveData = autocompleteRepository.getPredictionsResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_INPUT);
        Mockito.verify(placesApi).getPredictionsResponse(
            DEFAULT_INPUT,
            DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE,
            DEFAULT_RADIUS,
            DEFAULT_TYPE,
            DEFAULT_KEY
        );
        Mockito.verify(call).enqueue(callback.capture());
        callback.getValue().onResponse(call, retrofitResponse);

        PredictionsResponse result = LiveDataTestUtils.getValueForTesting(liveData);

        // Then
        assertEquals(expectedResponse, result);
    }

}
