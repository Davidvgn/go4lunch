package com.davidvignon.go4lunch.ui.details;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.Workmates;
import com.davidvignon.go4lunch.data.WorkmatesRepository;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesViewStates;

import java.util.ArrayList;
import java.util.List;

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

    @NonNull
    private final UserRepository userRepository;

    private final MutableLiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> restaurantPlaceId = new MutableLiveData<>();

    private final MediatorLiveData<RestaurantDetailsViewState> mediatorLiveData = new MediatorLiveData<>();

    @Inject
    public RestaurantDetailsViewModel(
        @NonNull PlaceDetailsRepository placeDetailsRepository,
        @NonNull UserRepository userRepository,
        @NonNull WorkmatesRepository workmatesRepository,
        @NonNull SavedStateHandle savedStateHandle
    ) {
        this.userRepository = userRepository;

        String placeId = savedStateHandle.get(KEY_PLACE_ID);
        LiveData<Boolean> isRestaurantSelectedLiveData = userRepository.isRestaurantSelectedLiveData(placeId);
        LiveData<Boolean> isRestaurantLikedLiveData = userRepository.isRestaurantLikedByUserLiveData(placeId);
        LiveData<DetailsResponse> detailsResponseLiveData = placeDetailsRepository.getDetailsResponseLiveData(placeId);
        LiveData<List<Workmates>> workmatesLiveData = workmatesRepository.getDataBaseUsers();

        restaurantPlaceId.setValue(placeId);

        mediatorLiveData.addSource(detailsResponseLiveData, new Observer<DetailsResponse>() {
            @Override
            public void onChanged(DetailsResponse detailsResponse) {
                combine(detailsResponse, isRestaurantLikedLiveData.getValue(), isRestaurantSelectedLiveData.getValue(), workmatesLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(isRestaurantLikedLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLiked) {
                combine(detailsResponseLiveData.getValue(), isLiked, isRestaurantSelectedLiveData.getValue(), workmatesLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(isRestaurantSelectedLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSelected) {
                combine(detailsResponseLiveData.getValue(), isRestaurantLikedLiveData.getValue(), isSelected, workmatesLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(workmatesLiveData, new Observer<List<Workmates>>() {
            @Override
            public void onChanged(List<Workmates> workmates) {
                combine(detailsResponseLiveData.getValue(), isRestaurantLikedLiveData.getValue(), isRestaurantSelectedLiveData.getValue(), workmates);
            }
        });

    }

    //todo david : gérer la couleur du selected qui est black et non blue / testUnit / chat/recherche

    public void combine(DetailsResponse response, Boolean isLiked, Boolean isSelected, List<Workmates> workmatesList) {
        List<WorkmatesViewStates> viewStates = new ArrayList<>();
        RestaurantDetailsViewState restaurantDetailsViewState;
        if (isLiked == null){
            isLiked = false;
        }
        if (isSelected == null){
            isSelected = false;
        }

        if (response != null) {
            Double initialRating = response.getResult().getRating();
            if (response.getResult().getName() != null
                && response.getResult().getVicinity() != null
                && response.getResult().getInternationalPhoneNumber() != null
                && response.getResult().getWebsite() != null
                && response.getResult().getPhotos() != null
                && response.getResult().getPhotos().get(0) != null
                && response.getResult().getPhotos().get(0).getPhotoReference() != null
                && initialRating != null
            ) {
                restaurantDetailsViewState = new RestaurantDetailsViewState(
                    response.getResult().getName(),
                    response.getResult().getVicinity(),
                    response.getResult().getInternationalPhoneNumber(),
                    response.getResult().getWebsite(),
                    response.getResult().getPhotos().get(0).getPhotoReference(),
                    (float) (initialRating * 3 / 5),
                    isSelected ? R.drawable.ic_baseline_check_circle_24 : R.drawable.ic_baseline_check_circle_outline_24,
                    isLiked ? "unlike" : "like",
                    isLiked ? R.drawable.ic_baseline_gold_star_rate_24 : R.drawable.ic_baseline_star_outline_24
                );

                    if (workmatesList != null) {
                        for (Workmates workmates : workmatesList) {
                            if (workmates.getSelectedRestaurant() != null && workmates.getSelectedRestaurant().equals(restaurantPlaceId.getValue()))
                                viewStates.add(new WorkmatesViewStates(
                                    workmates.getId(),
                                    workmates.getName(),
                                    workmates.getPicturePath()));
                        }
                        workmatesViewStatesLiveData.setValue(viewStates);
                }


                mediatorLiveData.setValue(restaurantDetailsViewState);
            }
        }
    }

    @NonNull
    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData() {
        return mediatorLiveData;
    }

    public LiveData<List<WorkmatesViewStates>> getWorkmatesViewStates() {
        return workmatesViewStatesLiveData;
    }

    public void onLikedButtonClicked() {
        userRepository.isRestaurantLikedByUserLiveData(restaurantPlaceId.getValue());
        userRepository.toggleRestaurantLiked(restaurantPlaceId.getValue());
    }

    public void selectRestaurant() {
        userRepository.isRestaurantSelectedLiveData(restaurantPlaceId.getValue());
        userRepository.toggleRestaurantSelected(restaurantPlaceId.getValue());
    }

}
