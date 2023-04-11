package com.davidvignon.go4lunch.ui.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.google_places.NearBySearchRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesViewState;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MapViewModelTest {

    private static final String DEFAULT_RESTAURANT_RESPONSE_NAME = "DEFAULT_RESTAURANT_RESPONSE_NAME";
    private static final String DEFAULT_RESTAURANT_RESPONSE_PLACE_ID = "DEFAULT_RESTAURANT_RESPONSE_PLACE_ID";
    private static final double DEFAULT_LONGITUDE_OFFSET = 5.0;
    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;
    private static final float DEFAULT_HUE = 0.0F;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final CurrentQueryRepository currentQueryRepository = Mockito.mock(CurrentQueryRepository.class);

    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> permissionMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData <String> currentRestaurantQueryLiveData = new MutableLiveData<>();


    private MapViewModel viewModel;

    @Before
    public void setUp() {

        permissionMutableLiveData.setValue(true);

        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Mockito.doReturn(permissionMutableLiveData).when(permissionRepository).isUserLocationGrantedLiveData();
        Mockito.doReturn(currentRestaurantQueryLiveData).when(currentQueryRepository).getCurrentRestaurantQuery();

        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        locationMutableLiveData.setValue(location);

        Mockito.doReturn(nearbySearchResponseMutableLiveData).when(nearBySearchRepository).getNearbySearchResponse(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        nearbySearchResponseMutableLiveData.setValue(getDefaultNearbySearchResponse());

        viewModel = new MapViewModel(permissionRepository, locationRepository, nearBySearchRepository, currentQueryRepository);
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

    @Test
    public void if_all_or_one_element_is_null_it_returns_no_response() {
        //Given
        nearbySearchResponseMutableLiveData.setValue(getNearbySearchResponseWithElementMissing());

        // When
        List<MapPoiViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getMapPoiViewStateLiveData());

        // Then
        assertTrue(viewStates.isEmpty());
    }

    @Test
    public void nominal_case_getFocusOnUser(){
        // When
        LatLng latLng = LiveDataTestUtils.getValueForTesting(viewModel.getFocusOnUser());

        // Then
        assertEquals(new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE),latLng);

    }

    @Test
    public void if_no_query_all_icons_are_red(){
        // Given
        currentRestaurantQueryLiveData.setValue(null);

        // When
        List<MapPoiViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getMapPoiViewStateLiveData());

        // Then
        assertEquals(getDefaultMapPoiViewStates(), viewStates);
    }

    @Test
    public void if_query_matches_icon_color_changes(){
        // Given
        currentRestaurantQueryLiveData.setValue("D0");

        // When
        List<MapPoiViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getMapPoiViewStateLiveData());

        // Then
        assertEquals(60.0F, viewStates.get(0).getHue(), 0.0);
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


    private NearbySearchResponse getNearbySearchResponseWithElementMissing() {
        List<RestaurantResponse> results = new ArrayList<>();

        //all elements are null
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
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        );

        //name is null
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
                null,
                null,
                getGeometryResponse(1),
                null,
                null,
                null,
                DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + 1,
                null
            )
        );


        //geometryResponse is null
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
                DEFAULT_RESTAURANT_RESPONSE_NAME + 2,
                null,
                null,
                null,
                null,
                null,
                DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + 2,
                null
            )
        );

        //placeId is null
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
                DEFAULT_RESTAURANT_RESPONSE_NAME + 3,
                null,
                getGeometryResponse(3),
                null,
                null,
                null,
                null,
                null
            )
        );

        return new NearbySearchResponse(
            null,
            null,
            results,
            null
        );
    }
    // endregion IN

    // region OUT
    private List<MapPoiViewState> getDefaultMapPoiViewStates() {
        List<MapPoiViewState> result = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            result.add(
                new MapPoiViewState(
                    DEFAULT_RESTAURANT_RESPONSE_PLACE_ID + i,
                    DEFAULT_RESTAURANT_RESPONSE_NAME + i,
                    (double) i,
                    DEFAULT_LONGITUDE_OFFSET + i,
                    DEFAULT_HUE

                )
            );
        }

        return result;
    }
    // endregion OUT
}
