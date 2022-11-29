package com.davidvignon.go4lunch.ui.details;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class RestaurantDetailsViewModelTest {

    private static final String DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PlaceDetailsRepository placeDetailsRepository = Mockito.mock(PlaceDetailsRepository.class);
    MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();

    private RestaurantDetailsViewModel viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(detailsResponseMutableLiveData).when(placeDetailsRepository)
            .getDetailsResponse(DEFAULT_PLACE_ID);

        viewModel = new RestaurantDetailsViewModel(placeDetailsRepository);
    }

    @Test
    public void initial_case() {
        // When
        RestaurantDetailsViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData(DEFAULT_PLACE_ID));


    }
}
