package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.nearby_places_model.GeometryResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.LocationResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.RestaurantResponse;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class NearBySearchRepositoryTest {

    private final PlacesApi placesApi = Mockito.mock(PlacesApi.class);

    private NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
    private MutableLiveData<Response<NearbySearchResponse>> response = new MutableLiveData<>();
    RestaurantResponse mRestaurantResponse = Mockito.mock(RestaurantResponse.class);
    MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();
    NearbySearchResponse nearbySearchResponse = Mockito.mock(NearbySearchResponse.class);


    @Before
    public void setUp() {
        nearBySearchRepository = new NearBySearchRepository(placesApi);
    }

    @Test
    public void test_getNearbySearchResponse() {
        double latitude = 43.0;
        double longitude = 56.0;
        String placeId = "1";
        String name = "Restaurant";
        GeometryResponse geometryResponse = Mockito.mock(GeometryResponse.class);
        LocationResponse locationResponse = Mockito.mock(LocationResponse.class);


        Mockito.doReturn(latitude).when(locationResponse).getLat();
        Mockito.doReturn(longitude).when(locationResponse).getLng();
        Mockito.doReturn(placeId).when(mRestaurantResponse).getPlaceId();
        Mockito.doReturn(name).when(mRestaurantResponse).getName();
        Mockito.doReturn(locationResponse).when(geometryResponse).getLocation();

        List<RestaurantResponse> listRest = new ArrayList<>();
        listRest.add(mRestaurantResponse);
        Mockito.doReturn(listRest).when(nearbySearchResponse).getResults();
        nearbySearchResponseMutableLiveData.setValue(nearbySearchResponse);

    }

}
