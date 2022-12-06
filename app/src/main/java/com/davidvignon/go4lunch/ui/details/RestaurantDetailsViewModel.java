package com.davidvignon.go4lunch.ui.details;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantDetailsViewModel extends ViewModel {

    private static final String KEY_PLACE_ID = "KEY_PLACE_ID";

    public static Intent navigate(@NonNull Context context, @NonNull String placeId) {
        Intent intent = new Intent(context, RestaurantDetailsActivity.class);
        intent.putExtra(KEY_PLACE_ID, placeId);
        return intent;
    }

    private final LiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData;

    private final MutableLiveData<Boolean> isSelectedMutableLiveData = new MutableLiveData<>();

    @Inject
    public RestaurantDetailsViewModel(
        @NonNull PlaceDetailsRepository placeDetailsRepository,
        @NonNull SavedStateHandle savedStateHandle
    ) {

        restaurantDetailsViewStateLiveData = Transformations.map(
            placeDetailsRepository.getDetailsResponseLiveData(savedStateHandle.get(KEY_PLACE_ID)),
            response -> {
                if (response.getResult() != null
                    && response.getResult().getName() != null
                    && response.getResult().getVicinity() != null
                    && response.getResult().getInternationalPhoneNumber() != null
                    && response.getResult().getWebsite() != null
                    && response.getResult().getPhotos() != null
                    && response.getResult().getPhotos().get(0).getPhotoReference() != null
                    && response.getResult().getRating() != null
                ) {
                    Double initialRating = response.getResult().getRating();

                    return new RestaurantDetailsViewState(
                        response.getResult().getName(),
                        response.getResult().getVicinity(),
                        response.getResult().getInternationalPhoneNumber(),
                        response.getResult().getWebsite(),
                        response.getResult().getPhotos().get(0).getPhotoReference(),
                        (float) (initialRating * 3 / 5)
                    );
                } else {
                    return null;
                }
            }
        );
    }

    @NonNull
    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData() {
        return restaurantDetailsViewStateLiveData;
    }

// (A faire : Si un resto est déjà choisi, penser à passer sa valeur à false si l'user en choisi un autre)

}
