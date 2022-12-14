////todo david repasser dessus après la mise en place du SavedStateHandle le 6/12/22
//package com.davidvignon.go4lunch.ui.details;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//import static org.mockito.ArgumentMatchers.any;
//
//import androidx.annotation.Nullable;
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.SavedStateHandle;
//
//import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
//import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
//import com.davidvignon.go4lunch.data.google_places.place_details.PhotosItem;
//import com.davidvignon.go4lunch.data.google_places.place_details.RestaurantDetailsResponse;
//import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RestaurantDetailsViewModelTest {
//
//    private static final String DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID";
//    private static final String DEFAULT_NAME = "DEFAULT_NAME";
//    private static final String DEFAULT_VICINITY = "DEFAULT_VICINITY";
//    private static final String DEFAULT_NUMBER = "DEFAULT_NUMBER";
//    private static final String DEFAULT_WEBSITE = "DEFAULT_WEBSITE";
//    private static final String DEFAULT_PHOTO = "DEFAULT_PHOTO";
//    private static final double DEFAULT_RATING = 3.4;
//
//    private static final String DEFAULT_KEY_PLACE_ID = "DEFAULT_KEY_PLACE_ID";
//
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    private final PlaceDetailsRepository placeDetailsRepository = Mockito.mock(PlaceDetailsRepository.class);
//    private final SavedStateHandle savedStateHandle = Mockito.mock(SavedStateHandle.class);
//    MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();
//
//    private RestaurantDetailsViewModel viewModel;
//
//
//    @Before
//    public void setUp() {
//        Mockito.doReturn(detailsResponseMutableLiveData).when(placeDetailsRepository)
//            .getDetailsResponseLiveData(DEFAULT_PLACE_ID);
//
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponse());
//
//        viewModel = new RestaurantDetailsViewModel(placeDetailsRepository, savedStateHandle);
//    }
//
//    @Test
//    public void initial_case() {
//        // When
//        RestaurantDetailsViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertEquals(getDefaultRestaurantViewState(), viewState);
//    }
//
//    @Test
//    public void if_all_elements_are_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithAllElementsMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//    @Test
//    public void if_name_is_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithNameMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//    @Test
//    public void if_vicinity_is_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithVicinityMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//    @Test
//    public void if_phoneNumber_is_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithPhoneNumberMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//    @Test
//    public void if_website_is_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithWebsiteMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//    @Test
//    public void if_photo_is_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithPhotoMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//    @Test
//    public void if_rating_elements_are_null_it_returns_no_response() {
//        // Given
//        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithRatingMissing());
//
//        // When
//        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());
//
//        // Then
//        assertNull(viewStates);
//    }
//
//    // region IN
//    private DetailsResponse getDefaultDetailsResponse() {
////        RestaurantDetailsResponse result = new RestaurantDetailsResponse(
////            0,
////            null,
////            true,
////            true,
////            null,
////            DEFAULT_RATING,
////            null,
////            true,
////            getDefaultPhotos(),
////            null,
////            null,
////            true,
////            null,
////            3,
////            null,
////            true,
////            null,
////            true,
////            true,
////            true,
////            null,
////            DEFAULT_PLACE_ID,
////            true,
////            true,
////            null,
////            DEFAULT_WEBSITE,
////            null,
////            null,
////            null,
////            0,
////            DEFAULT_NAME,
////            null,
////            null,
////            DEFAULT_VICINITY,
////            null,
////            null,
////            DEFAULT_NUMBER,
////            true
////        );
//        return new DetailsResponse(
//            Mockito.mock(RestaurantDetailsResponse.class), // TODO DAVID FILL THIS MOCK WITH DEFAULT VALUES
//            null,
//            null
//        );
//    }
//
//    private List<PhotosItem> getDefaultPhotos() {
//        List<PhotosItem> photosItem = new ArrayList<>();
//
//        photosItem.add(new PhotosItem(DEFAULT_PHOTO));
//        return photosItem;
//    }
//
//    private DetailsResponse getDefaultDetailsResponseWithNameMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            null,
//            DEFAULT_VICINITY,
//            DEFAULT_NUMBER,
//            DEFAULT_WEBSITE,
//            getDefaultPhotos(),
//            DEFAULT_RATING
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//    private DetailsResponse getDefaultDetailsResponseWithVicinityMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            DEFAULT_NAME,
//            null,
//            DEFAULT_NUMBER,
//            DEFAULT_WEBSITE,
//            getDefaultPhotos(),
//            DEFAULT_RATING
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//    private DetailsResponse getDefaultDetailsResponseWithPhoneNumberMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            DEFAULT_NAME,
//            DEFAULT_VICINITY,
//            null,
//            DEFAULT_WEBSITE,
//            getDefaultPhotos(),
//            DEFAULT_RATING
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//    private DetailsResponse getDefaultDetailsResponseWithWebsiteMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            DEFAULT_NAME,
//            DEFAULT_VICINITY,
//            DEFAULT_NUMBER,
//            null,
//            getDefaultPhotos(),
//            DEFAULT_RATING
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//    private DetailsResponse getDefaultDetailsResponseWithPhotoMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            DEFAULT_NAME,
//            DEFAULT_VICINITY,
//            DEFAULT_NUMBER,
//            DEFAULT_WEBSITE,
//            null,
//            DEFAULT_RATING
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//    private DetailsResponse getDefaultDetailsResponseWithRatingMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            DEFAULT_NAME,
//            DEFAULT_VICINITY,
//            DEFAULT_NUMBER,
//            DEFAULT_WEBSITE,
//            getDefaultPhotos(),
//            null
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//    private DetailsResponse getDefaultDetailsResponseWithAllElementsMissing() {
//        RestaurantDetailsResponse result = getDefaultRestaurant(
//            DEFAULT_PLACE_ID,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null
//        );
//
//        return new DetailsResponse(
//            result,
//            null,
//            null
//        );
//    }
//
//
//    private RestaurantDetailsResponse getDefaultRestaurant(
//        @Nullable String placeId,
//        @Nullable String name,
//        @Nullable String vicinity,
//        @Nullable String phoneNumber,
//        @Nullable String website,
//        @Nullable List<PhotosItem> photo,
//        @Nullable Double rating
//    ) {
//
//        RestaurantDetailsResponse response = Mockito.mock(RestaurantDetailsResponse.class);
//        Mockito.doReturn(placeId).when(response).getPlaceId();
//        Mockito.doReturn(name).when(response).getName();
//        Mockito.doReturn(vicinity).when(response).getVicinity();
//        Mockito.doReturn(phoneNumber).when(response).getInternationalPhoneNumber();
//        Mockito.doReturn(website).when(response).getWebsite();
//        Mockito.doReturn(photo).when(response).getPhotos();
//        Mockito.doReturn(rating).when(response).getRating();
//
//        return response;
//
////         new RestaurantDetailsResponse(
////            0,
////            null,
////            true,
////            true,
////            null,
////            rating,
////            null,
////            true,
////            photo,
////            null,
////            null,
////            true,
////            null,
////            3,
////            null,
////            true,
////            null,
////            true,
////            true,
////            true,
////            null,
////            placeId,
////            true,
////            true,
////            null,
////            website,
////            null,
////            null,
////            null,
////            0,
////            name,
////            null,
////            null,
////            vicinity,
////            null,
////            null,
////            phoneNumber,
////            true
////        );
//    }
//    // endregion IN
//
//
//    // region OUT
//    private RestaurantDetailsViewState getDefaultRestaurantViewState() {
//        return new RestaurantDetailsViewState(
//            DEFAULT_NAME,
//            DEFAULT_VICINITY,
//            DEFAULT_NUMBER,
//            DEFAULT_WEBSITE,
//            DEFAULT_PHOTO,
//            2.04F
//        );
//    }
//    // endregion OUT
//}
