package com.davidvignon.go4lunch.data.google_places;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Call;

@RunWith(MockitoJUnitRunner.class)
public class NearBySearchRepositoryTest {

    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;
    private static final String DEFAULT_RADIUS = "1500";
    private static final String DEFAULT_TYPE = "restaurant";
    private static final String DEFAULT_KEY = "123";


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    PlacesApi placesApi;

    private NearBySearchRepository nearBySearchRepository;

    @Before
    public void setUp() {
        nearBySearchRepository = new NearBySearchRepository(placesApi);

    }

}

