package com.davidvignon.go4lunch.ui.details;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantDetailsViewModel extends ViewModel {

    @NonNull
    private final PlaceDetailsRepository placeDetailsRepository;

    LiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData;

    MutableLiveData<Boolean> isSelectedMutableLiveData = new MutableLiveData<>();

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
            place, input -> placeDetailsRepository.getDetailsResponse(input)
        );

        return Transformations.map(detailsResponseLiveData, response -> {
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
            } else return null;
        });
    }

// (A faire : Si un resto est déjà choisi, penser à passer sa valeur à false si l'user en choisi un autre)

}
