package com.davidvignon.go4lunch.ui.details;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.FirestoreRepository;
import com.davidvignon.go4lunch.data.Workmates;
import com.davidvignon.go4lunch.data.WorkmatesRepository;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesViewStates;
import com.google.firebase.auth.FirebaseAuth;

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
    @NonNull
    private final FirestoreRepository firestoreRepository;

    private final MutableLiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> restaurantPlaceId = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isLikedMutableLiveData = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isSelectedMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();

    public final MediatorLiveData<RestaurantDetailsViewState> mediatorLiveData = new MediatorLiveData<>();

    @Inject
    public RestaurantDetailsViewModel(
        @NonNull PlaceDetailsRepository placeDetailsRepository,
        @NonNull UserRepository userRepository,
        @NonNull WorkmatesRepository workmatesRepository,
        @NonNull FirestoreRepository firestoreRepository,
        @NonNull SavedStateHandle savedStateHandle
    ) {
        this.userRepository = userRepository;
        this.firestoreRepository = firestoreRepository;

        LiveData<List<Workmates>> workmatesLiveData = workmatesRepository.getDataBaseUsers();

        //todo david : besoin de double click parfois pour liker un resto

        if (isLikedMutableLiveData.getValue() == null){
            isLikedMutableLiveData.setValue(false);
        }

        if (isSelectedMutableLiveData.getValue() == null){
            isSelectedMutableLiveData.setValue(false);
        }


        restaurantPlaceId.setValue(savedStateHandle.get(KEY_PLACE_ID));

        mediatorLiveData.addSource(placeDetailsRepository.getDetailsResponseLiveData(savedStateHandle.get(KEY_PLACE_ID)), new Observer<DetailsResponse>() {
            @Override
            public void onChanged(DetailsResponse detailsResponse) {
                combine(detailsResponse, isLikedMutableLiveData.getValue(), isSelectedMutableLiveData.getValue(), workmatesLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(firestoreRepository.isRestaurantLikedByUser(savedStateHandle.get(KEY_PLACE_ID)), new Observer<Boolean>() {//todo Nino: la source à mettre dans isLikeMutableLIvedata ou alors on peut/doit call direct dans le repo???
            @Override
            public void onChanged(Boolean isLiked) {
                combine(detailsResponseMutableLiveData.getValue(), isLiked, isSelectedMutableLiveData.getValue(), workmatesLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(isSelectedMutableLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSelected) {
                combine(detailsResponseMutableLiveData.getValue(), isLikedMutableLiveData.getValue(), isSelected, workmatesLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(workmatesLiveData, new Observer<List<Workmates>>() {
            @Override
            public void onChanged(List<Workmates> workmates) {
                combine(detailsResponseMutableLiveData.getValue(), isLikedMutableLiveData.getValue(), isSelectedMutableLiveData.getValue(), workmates);
            }
        });

    }

    public void combine(DetailsResponse response, Boolean isLiked, Boolean isSelected, List<Workmates> workmatesList) {
        List<WorkmatesViewStates> viewStates = new ArrayList<>();
        RestaurantDetailsViewState restaurantDetailsViewState;

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

        if (response!=null){
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
                    isSelected,
                    isLiked ? "unlike" : "like",
                    isLiked ? R.drawable.ic_baseline_red_star_rate_24 : R.drawable.ic_baseline_star_border_24
                );
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
        Boolean previousValue = isLikedMutableLiveData.getValue();
        if (previousValue == null) {
            previousValue = false;
        }
        isLikedMutableLiveData.setValue(!previousValue);
        boolean newValue = isLikedMutableLiveData.getValue();
        if (newValue) {
            userRepository.likeRestaurant(restaurantPlaceId.getValue());
        } else {
            userRepository.unLikeRestaurant(restaurantPlaceId.getValue());
        }
    }

    public void selectRestaurant() {
        Boolean previousValue = isSelectedMutableLiveData.getValue();
        if (previousValue == null) {
            previousValue = false;
        }
        isSelectedMutableLiveData.setValue(!previousValue);
        userRepository.selectRestaurant(restaurantPlaceId.getValue());
    }

}
