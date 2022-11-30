package com.davidvignon.go4lunch.ui.details;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
import com.davidvignon.go4lunch.data.google_places.place_details.PhotosItem;
import com.davidvignon.go4lunch.data.google_places.place_details.RestaurantDetailsResponse;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsViewModelTest {

    private static final String DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_VICINITY = "DEFAULT_VICINITY";
    private static final String DEFAULT_NUMBER = "DEFAULT_NUMBER";
    private static final String DEFAULT_WEBSITE = "DEFAULT_WEBSITE";
    private static final String DEFAULT_PHOTO = "DEFAULT_PHOTO";
    private static final float DEFAULT_RATING = 3.4F;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PlaceDetailsRepository placeDetailsRepository = Mockito.mock(PlaceDetailsRepository.class);
    MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();

    private RestaurantDetailsViewModel viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(detailsResponseMutableLiveData).when(placeDetailsRepository)
            .getDetailsResponse(DEFAULT_PLACE_ID);

        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponse());

        viewModel = new RestaurantDetailsViewModel(placeDetailsRepository);
    }

    @Test
    public void initial_case() {
        // When
        RestaurantDetailsViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData(DEFAULT_PLACE_ID));


        // Then
        assertEquals(getDefaultRestaurantViewState(), viewState);
    }

    // region IN
    private DetailsResponse getDefaultDetailsResponse(){
        RestaurantDetailsResponse result = new RestaurantDetailsResponse(
            0,
            null,
            true,
            true,
            null,
            DEFAULT_RATING,
            null,
            true,
            getDefaultPhotos(),
            null,
            null,
            true,
            null,
            3,
            null,
            true,
            null,
            true,
            true,
            true,
            null,
            DEFAULT_PLACE_ID,
            true,
            true,
            null,
            DEFAULT_WEBSITE,
            null,
            null,
            null,
            0,
            DEFAULT_NAME,
            null,
            null,
            DEFAULT_VICINITY,
            null,
            null,
            DEFAULT_NUMBER,
            true
        );
        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private List<PhotosItem> getDefaultPhotos() {
        List<PhotosItem> photosItem= new ArrayList<>();

        photosItem.add(new PhotosItem(DEFAULT_PHOTO));
        return photosItem;
    }
    // endregion IN


    // region OUT
    private RestaurantDetailsViewState getDefaultRestaurantViewState(){
        RestaurantDetailsViewState result = new RestaurantDetailsViewState(
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            DEFAULT_PHOTO,
            2.04F
        );


        return result;
    }

    // endregion OUT
}
