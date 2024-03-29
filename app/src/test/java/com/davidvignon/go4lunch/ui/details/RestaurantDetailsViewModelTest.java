package com.davidvignon.go4lunch.ui.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
import com.davidvignon.go4lunch.data.google_places.place_details.PhotosItem;
import com.davidvignon.go4lunch.data.google_places.place_details.RestaurantDetailsResponse;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsViewModelTest {

    private static final String DEFAULT_KEY_PLACE_ID = "DEFAULT_KEY_PLACE_ID";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_VICINITY = "DEFAULT_VICINITY";
    private static final String DEFAULT_NUMBER = "DEFAULT_NUMBER";
    private static final String DEFAULT_WEBSITE = "DEFAULT_WEBSITE";
    private static final String DEFAULT_PHOTO = "DEFAULT_PHOTO";

    private static final double DEFAULT_RATING = 3.4;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    private final Application application = Mockito.mock(Application.class);
    private final PlaceDetailsRepository placeDetailsRepository = Mockito.mock(PlaceDetailsRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final WorkmateRepository workmateRepository = Mockito.mock(WorkmateRepository.class);
    private final SavedStateHandle savedStateHandle = Mockito.mock(SavedStateHandle.class);
    private final MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRestaurantLikedMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRestaurantSelectedMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmateListMutableLiveData = new MutableLiveData<>();

    private RestaurantDetailsViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn("unlike").when(application).getString(R.string.unlike_restaurant);
        Mockito.doReturn("like").when(application).getString(R.string.like_restaurant);

        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponse());
        isRestaurantLikedMutableLiveData.setValue(false);
        isRestaurantSelectedMutableLiveData.setValue(false);
        Mockito.doReturn(DEFAULT_KEY_PLACE_ID).when(savedStateHandle).get("KEY_PLACE_ID");

        Mockito.doReturn(detailsResponseMutableLiveData).when(placeDetailsRepository).getDetailsResponseLiveData(DEFAULT_KEY_PLACE_ID);
        Mockito.doReturn(isRestaurantLikedMutableLiveData).when(userRepository).isRestaurantLikedByUserLiveData(DEFAULT_KEY_PLACE_ID);
        Mockito.doReturn(isRestaurantSelectedMutableLiveData).when(userRepository).isRestaurantSelectedLiveData(DEFAULT_KEY_PLACE_ID);
        Mockito.doReturn(workmateListMutableLiveData).when(workmateRepository).getUserListGoingToLiveData(DEFAULT_KEY_PLACE_ID);

        viewModel = new RestaurantDetailsViewModel(application, placeDetailsRepository, userRepository, workmateRepository, savedStateHandle);
    }

    @Test
    public void nominal_case() {
        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertEquals(getDefaultRestaurantViewState(), viewStates);
    }

    @Test
    public void if_all_elements_are_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithAllElementsMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void if_name_is_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithNameMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void if_vicinity_is_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithVicinityMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void if_phoneNumber_is_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithPhoneNumberMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void if_website_is_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithWebsiteMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void if_photo_is_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithPhotoMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void if_rating_elements_are_null_it_returns_no_response() {
        // Given
        detailsResponseMutableLiveData.setValue(getDefaultDetailsResponseWithRatingMissing());

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertNull(viewStates);
    }

    @Test
    public void all_boolean_are_true_icons_and_text_change() {
        // Given
        isRestaurantLikedMutableLiveData.setValue(true);
        isRestaurantSelectedMutableLiveData.setValue(true);

        // When
        RestaurantDetailsViewState viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getRestaurantDetailsViewStateLiveData());

        // Then
        assertEquals(getRestaurantViewStateWithAllBooleanAtTrue(), viewStates);

    }

    // region IN
    private DetailsResponse getDefaultDetailsResponse() {

        RestaurantDetailsResponse response = Mockito.mock(RestaurantDetailsResponse.class);
        Mockito.doReturn(DEFAULT_KEY_PLACE_ID).when(response).getPlaceId();
        Mockito.doReturn(DEFAULT_NAME).when(response).getName();
        Mockito.doReturn(DEFAULT_VICINITY).when(response).getVicinity();
        Mockito.doReturn(DEFAULT_NUMBER).when(response).getInternationalPhoneNumber();
        Mockito.doReturn(DEFAULT_WEBSITE).when(response).getWebsite();
        Mockito.doReturn(getDefaultPhotos()).when(response).getPhotos();
        return new DetailsResponse(
            response,
            null,
            null
        );
    }

    private List<PhotosItem> getDefaultPhotos() {
        List<PhotosItem> photosItem = new ArrayList<>();

        photosItem.add(new PhotosItem(DEFAULT_PHOTO));
        return photosItem;
    }

    private DetailsResponse getDefaultDetailsResponseWithNameMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            null,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            getDefaultPhotos(),
            DEFAULT_RATING
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private DetailsResponse getDefaultDetailsResponseWithVicinityMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            DEFAULT_NAME,
            null,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            getDefaultPhotos(),
            DEFAULT_RATING
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private DetailsResponse getDefaultDetailsResponseWithPhoneNumberMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            null,
            DEFAULT_WEBSITE,
            getDefaultPhotos(),
            DEFAULT_RATING
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private DetailsResponse getDefaultDetailsResponseWithWebsiteMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            null,
            getDefaultPhotos(),
            DEFAULT_RATING
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private DetailsResponse getDefaultDetailsResponseWithPhotoMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            null,
            DEFAULT_RATING
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private DetailsResponse getDefaultDetailsResponseWithRatingMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            getDefaultPhotos(),
            null
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private DetailsResponse getDefaultDetailsResponseWithAllElementsMissing() {
        RestaurantDetailsResponse result = getDefaultRestaurant(
            DEFAULT_KEY_PLACE_ID,
            null,
            null,
            null,
            null,
            null,
            null
        );

        return new DetailsResponse(
            result,
            null,
            null
        );
    }

    private RestaurantDetailsResponse getDefaultRestaurant(
        @Nullable String placeId,
        @Nullable String name,
        @Nullable String vicinity,
        @Nullable String phoneNumber,
        @Nullable String website,
        @Nullable List<PhotosItem> photo,
        @Nullable Double rating
    ) {

        RestaurantDetailsResponse response = Mockito.mock(RestaurantDetailsResponse.class);
        Mockito.doReturn(placeId).when(response).getPlaceId();
        Mockito.doReturn(name).when(response).getName();
        Mockito.doReturn(vicinity).when(response).getVicinity();
        Mockito.doReturn(phoneNumber).when(response).getInternationalPhoneNumber();
        Mockito.doReturn(website).when(response).getWebsite();
        Mockito.doReturn(photo).when(response).getPhotos();
        Mockito.doReturn(rating).when(response).getRating();

        return response;
    }
    // endregion IN


    // region OUT
    private RestaurantDetailsViewState getDefaultRestaurantViewState() {
        return new RestaurantDetailsViewState(
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            DEFAULT_PHOTO,
            0.0F,
            R.drawable.ic_baseline_check_circle_outline_24,
            R.color.white,
            application.getString(R.string.like_restaurant),
            R.drawable.ic_baseline_star_outline_24
        );
    }

    private RestaurantDetailsViewState getRestaurantViewStateWithAllBooleanAtTrue() {
        return new RestaurantDetailsViewState(
            DEFAULT_NAME,
            DEFAULT_VICINITY,
            DEFAULT_NUMBER,
            DEFAULT_WEBSITE,
            DEFAULT_PHOTO,
            0.0F,
            R.drawable.ic_baseline_check_circle_24,
            R.color.teal_200,
            application.getString(R.string.unlike_restaurant),
            R.drawable.ic_baseline_gold_star_rate_24
        );
    }
    // endregion OUT
}
