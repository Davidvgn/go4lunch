package com.davidvignon.go4lunch.ui.workmates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final Application application;

    private final MediatorLiveData<List<WorkmatesViewState>> mediatorLiveData = new MediatorLiveData<>();

    @NonNull
    private final FirebaseAuth firebaseAuth;

    @Inject
    public WorkmatesViewModel(Application application, @NonNull WorkmateRepository workmateRepository, @NonNull FirebaseAuth firebaseAuth, @NonNull CurrentQueryRepository currentQueryRepository
    ) {
        this.application = application;
        this.firebaseAuth = firebaseAuth;

        LiveData<List<Workmate>> dataBaseUsersLiveData = workmateRepository.getDataBaseUsersLiveData();
        LiveData<String> currentRestaurantQueryLiveData = currentQueryRepository.getCurrentRestaurantQuery();

        mediatorLiveData.addSource(dataBaseUsersLiveData, workmates -> combine(workmates, currentRestaurantQueryLiveData.getValue()));
        mediatorLiveData.addSource(currentRestaurantQueryLiveData, query -> combine(dataBaseUsersLiveData.getValue(), query));
    }

    @NonNull
    public LiveData<List<WorkmatesViewState>> getWorkmatesViewStatesLiveData() {
        return mediatorLiveData;
    }


    private void combine(List<Workmate> workmatesList, String searchedQuery) {

        if (workmatesList != null) {
            List<WorkmatesViewState> viewStates = new ArrayList<>();

            for (Workmate workmate : workmatesList) {
                if (firebaseAuth.getCurrentUser() != null){
                if (!workmate.getId().equals(firebaseAuth.getCurrentUser().getUid())) {
                    String workmateName = (workmate.getSelectedRestaurant() == null) ? workmate.getName() + application.getString(R.string.no_selected_restaurant) : workmate.getName();
                    if (searchedQuery == null || (workmate.getSelectedRestaurantName() != null && isRestaurantNamePartialMatchForQuery(workmate.getSelectedRestaurantName(), searchedQuery))) {

                        viewStates.add(
                            new WorkmatesViewState(
                                workmate.getId(),
                                workmateName,
                                workmate.getPicturePath(),
                                workmate.getSelectedRestaurant(),
                                workmate.getSelectedRestaurantName()
                            )
                        );
                    }
                }
                    mediatorLiveData.setValue(viewStates);
                }
            }
        }
    }

    private boolean isRestaurantNamePartialMatchForQuery(final @NonNull String restaurantName, final @NonNull String query) {
        String restaurantNameLowercase = restaurantName.toLowerCase();
        String queryLowercase = query.toLowerCase();
        int i = 0;
        for (int j = 0; j < restaurantNameLowercase.length() && i < queryLowercase.length(); j++) {
            if (restaurantNameLowercase.charAt(j) == queryLowercase.charAt(i)) {
                i++;
            }
        }
        return i == queryLowercase.length();
    }
}


