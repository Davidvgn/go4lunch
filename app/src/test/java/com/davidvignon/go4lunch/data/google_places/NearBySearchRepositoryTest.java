package com.davidvignon.go4lunch.data.google_places;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NearBySearchRepositoryTest {


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

//    private NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
//    private NearBySearchRepository nearBySearchRepository;
    private final PlacesApi placesApi = Mockito.mock(PlacesApi.class);

    MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();


    @Before
    public void setUp() {
//        nearBySearchRepository = new NearBySearchRepository(placesApi);

    }
}

