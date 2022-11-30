package com.davidvignon.go4lunch.ui.details;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantDetailsViewModel extends ViewModel {

    @NonNull
    private final PlaceDetailsRepository placeDetailsRepository;

    LiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData;

    MutableLiveData<String> placeIdMutableliveData = new MutableLiveData<>();

    @Inject
    public RestaurantDetailsViewModel(@NonNull PlaceDetailsRepository placeDetailsRepository) {
        this.placeDetailsRepository = placeDetailsRepository;

        restaurantDetailsViewStateLiveData = bindingViewState(placeIdMutableliveData);

    }

    @NonNull
    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData(String placeId) {
        placeIdMutableliveData.setValue(placeId);
        return restaurantDetailsViewStateLiveData;
    }

    public LiveData<RestaurantDetailsViewState> bindingViewState(LiveData<String> place) {
        LiveData<DetailsResponse> detailsResponseLiveData = Transformations.switchMap(
            place, new Function<String, LiveData<DetailsResponse>>() {
                @Override
                public LiveData<DetailsResponse> apply(String input) {
                    return placeDetailsRepository.getDetailsResponse(input);
                }
            }
        );

        return Transformations.map(detailsResponseLiveData, new Function<DetailsResponse, RestaurantDetailsViewState>() {
            @Override
            public RestaurantDetailsViewState apply(DetailsResponse response) {

                double initialRating = response.getResult().getRating();

                RestaurantDetailsViewState viewState = new RestaurantDetailsViewState(
                    response.getResult().getName(),
                    response.getResult().getVicinity(),
                    response.getResult().getInternationalPhoneNumber(),
                    response.getResult().getWebsite(),
                    response.getResult().getPhotos().get(0).getPhotoReference(),
                    (float) (initialRating * 3 / 5)
                );
                return viewState;
            }
        });
    }
}
