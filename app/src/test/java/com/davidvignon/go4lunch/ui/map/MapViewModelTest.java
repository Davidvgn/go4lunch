package com.davidvignon.go4lunch.ui.map;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MapViewModelTest {

    private static final String DEFAULT_RESTAURANT_RESPONSE_NAME = "DEFAULT_RESTAURANT_RESPONSE_NAME";
    private static final String DEFAULT_RESTAURANT_RESPONSE_PLACE_ID = "DEFAULT_RESTAURANT_RESPONSE_PLACE_ID";
    private static final double DEFAULT_LONGITUDE_OFFSET = 46.0;
    private static final double DEFAULT_LATITUDE = 47.4055044477;
    private static final double DEFAULT_LONGITUDE = 0.698770997973;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

    private MapViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        locationMutableLiveData.setValue(location);

        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponse());

        viewModel = new MapViewModel(locationRepository, nearBySearchRepository);
    }

    @Test
    public void initial_case() {
        // When
        List<MapPoiViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getMapPoiViewStateLiveData());

        // Then
        assertEquals(
            getDefaultMapPoiViewStates(),
            viewStates
        );
    }

    // region IN
    private NearbySearchResponse getDefaultNearbySearchResponse() {
        List<RestaurantResponse> results = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            results.add(
                new RestaurantResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
                    null,
                    getGeometryResponse(i),
                    null,
                    null,
                    null,
                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
                    null
                )
            );
        }

        return new NearbySearchResponse(
            null,
            null,
            results,
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

    // endregion IN

    // region OUT
    private  List<MapPoiViewState> getDefaultMapPoiViewStates() {
        List<MapPoiViewState> result = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            result.add(
                new MapPoiViewState(
                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
                    DEFAULT_LONGITUDE_OFFSET + i,
                    (double) i
                )
            );
        }

        return result;
    }
    // endregion OUT
}
