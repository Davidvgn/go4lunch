package com.davidvignon.go4lunch.ui.map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapFragmentViewModelTest {

    private static final double DEFAULT_LATTITUDE = 47.4055044477;
    private static final double DEFAULT_LONGITUDE = 0.698770997973;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);

    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);

    private final Location location = Mockito.mock(android.location.Location.class);
    private final NearbySearchResponse nearbySearchResponse = Mockito.mock(NearbySearchResponse.class);



    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MapPoiViewState>> test = new MutableLiveData<>();
    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

    RestaurantResponse result = Mockito.mock(RestaurantResponse.class);

    private MapFragmentViewModel viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();

        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATTITUDE,DEFAULT_LONGITUDE);

        Mockito.doReturn(DEFAULT_LATTITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();

        Mockito.doReturn(getDefaultPois()).when(nearbySearchResponse).getResults();

        locationMutableLiveData.setValue(location);

        Mockito.doReturn(getDefaultPois()).when(nearbySearchResponse).getResults();
        nearbySearchResponseMutableLiveData.setValue(nearbySearchResponse);

        viewModel = new MapFragmentViewModel(locationRepository, nearBySearchRepository);
        given(viewModel.getMapPoiViewStateLiveData()).willReturn(getDefaultPoisLiveData());
    }

    @Test
    public void test_location() {
        //Given
        double latitude;
        double longitude;

        //When
        MapPoiViewState mapPoiViewState = new MapPoiViewState("1", "Restaurant",47.4055044477,0.698770997973);

        latitude = mapPoiViewState.getLatitude();
        longitude = mapPoiViewState.getLongitude();

        //Then
        assertEquals(latitude, location.getLatitude(), 0.0);
        assertEquals(longitude, location.getLongitude(),0.0);
    }

    @Test
    public void test_nearBy() {

    }




    @NonNull
    private List<MapPoiViewState> getDefaultPois() {
        List<MapPoiViewState> poiViewStates = new ArrayList<>();

        poiViewStates.add(new MapPoiViewState("1", "Restaurant 1", 41.00, 51.00));
        poiViewStates.add(new MapPoiViewState("2", "Restaurant 2", 42.00, 52.00));
        poiViewStates.add(new MapPoiViewState("3", "Restaurant 3", 43.00, 53.00));

        return poiViewStates;
    }

    @NonNull
    private LiveData<List<MapPoiViewState>> getDefaultPoisLiveData() {
        List<MapPoiViewState> poiViewStates = new ArrayList<>();

        poiViewStates.add(new MapPoiViewState("1", "Restaurant 1", 41.00, 51.00));
        poiViewStates.add(new MapPoiViewState("2", "Restaurant 2", 42.00, 52.00));
        poiViewStates.add(new MapPoiViewState("3", "Restaurant 3", 43.00, 53.00));

        test.setValue(poiViewStates);
        return test;
    }
}
