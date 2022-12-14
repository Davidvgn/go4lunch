package com.davidvignon.go4lunch.ui.details;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.PlaceDetailsRepository;
import com.davidvignon.go4lunch.data.users.User;
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

    private final UserRepository userRepository;

    // TODO David Utiliser un MediatorLiveData pour avoir l'état des boutons (liked / isGoing) ET les workmates
    private final LiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData;

    private final LiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData;

    private final MutableLiveData<String> restaurantPlaceId = new MutableLiveData<>();

    // TODO DAVID Cette livedata vient du repo (firestore)
    public final MutableLiveData<Boolean> isLikedMutableLiveData = new MutableLiveData<>();

    // TODO DAVID Cette livedata vient du repo (firestore)
    public final MutableLiveData<Boolean> isSelectedMutableLiveData = new MutableLiveData<>();


    public final MutableLiveData<Integer> visibleornot = new MutableLiveData<>();

    @Inject
    public RestaurantDetailsViewModel(
        @NonNull PlaceDetailsRepository placeDetailsRepository,
        @NonNull UserRepository userRepository,
        @NonNull SavedStateHandle savedStateHandle
    ) {
        this.userRepository = userRepository;

        if (isSelectedMutableLiveData.getValue() == null) {
            isSelectedMutableLiveData.setValue(false);
        }
        if (isLikedMutableLiveData.getValue() == null) {
            isLikedMutableLiveData.setValue(false);
        }
        restaurantPlaceId.setValue(savedStateHandle.get(KEY_PLACE_ID));

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

                    boolean isLiked = isLikedMutableLiveData.getValue() != null && isLikedMutableLiveData.getValue();

                    return new RestaurantDetailsViewState(
                        response.getResult().getName(),
                        response.getResult().getVicinity(),
                        response.getResult().getInternationalPhoneNumber(),
                        response.getResult().getWebsite(),
                        response.getResult().getPhotos().get(0).getPhotoReference(),
                        (float) (initialRating * 3 / 5),
                        isSelectedMutableLiveData.getValue(),
                        isLiked ? "liked" : "not liked",
                        isLiked ? R.drawable.ic_baseline_red_star_rate_24 : R.drawable.ic_baseline_star_border_24
                    );
                } else {
                    return null;
                }
            }
        );

        LiveData<List<User>> dataBaseUsersLiveData = userRepository.getDataBaseUsers();
        workmatesViewStatesLiveData = bindViewState(dataBaseUsersLiveData);

    }

    @NonNull
    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData() {
        return restaurantDetailsViewStateLiveData;
    }
    @NonNull
    public LiveData<List<WorkmatesViewStates>> getWorkmatesViewStatesLiveData() {
        return workmatesViewStatesLiveData;
    }

    public void restaurantIsFavoriteOrNot() {
        if (isLikedMutableLiveData.getValue() != null) {
            boolean isFavorite = isLikedMutableLiveData.getValue();
            isLikedMutableLiveData.setValue(!isFavorite);

            if (isLikedMutableLiveData.getValue()) {
                userRepository.likeRestaurant(restaurantPlaceId.getValue());

            } else {
                userRepository.unLikeRestaurant(restaurantPlaceId.getValue());
            }
        }
    }

    public void selectRestaurant() {
        if (isSelectedMutableLiveData.getValue() != null) {
            boolean isFavorite = isSelectedMutableLiveData.getValue();
            isSelectedMutableLiveData.setValue(!isFavorite);
            if (isSelectedMutableLiveData.getValue()) {
                userRepository.selectRestaurant(restaurantPlaceId.getValue());
            } else {
                userRepository.unSelectRestaurant();
            }
        }
    }
    @NonNull
    private LiveData<List<WorkmatesViewStates>> bindViewState(LiveData<List<User>> dataBaseUsersLiveData) {
        return Transformations.map(dataBaseUsersLiveData, new Function<List<User>, List<WorkmatesViewStates>>() {
            @Override
            public List<WorkmatesViewStates> apply(List<User> result) {
                List<WorkmatesViewStates> viewStates = new ArrayList<>();

                Log.d("DavidVgn", "apply: "+ result.get(0).getSelectedRestaurant());
                Log.d("DavidVgn", "apply1: "+ result.get(1).getSelectedRestaurant());
                Log.d("DavidVgn", "apply: "+ restaurantPlaceId.getValue());
                Log.d("DavidVgn", "apply: "+ userRepository.getUserSelectedRestaurant().getValue());

//                if (result != null) {
//                    for (int i = 0; i < result.size(); i++) {
//                        if (result.get(i).getFavoritesRestaurants().equals(restaurantplaceid.getValue())){
//                            result.remove(i);
//                            break;
//                        }
//                    }
//                    for (User user : result) {
//                        viewStates.add(new WorkmatesViewStates(
//                            user.getId(),
//                            user.getName(),
//                            user.getPicturePath()));
//                    }
//                }
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).getFavoritesRestaurants().equals(restaurantPlaceId)){
                            for (User user : result) {
                                viewStates.add(new WorkmatesViewStates(
                                    user.getId(),
                                    user.getName(),
                                    user.getPicturePath()));
                            }
                        } else {
                            result.remove(i);
                        }
                    }

                }
                return viewStates;

            }
        });
    }

    public void onLikedButtonClicked() {
        // TODO DAVID Repo à mettre à jour
    }
}
