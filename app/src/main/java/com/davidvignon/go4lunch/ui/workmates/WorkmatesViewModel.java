package com.davidvignon.go4lunch.ui.workmates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final Application application;

    private final LiveData<List<WorkmatesViewState>> workmatesViewStatesLiveData;

    @NonNull
    private final FirebaseUser firebaseUser;

    @Inject
    public WorkmatesViewModel(Application application, WorkmateRepository workmateRepository, @NonNull FirebaseUser firebaseUser) {
        this.application = application;
        this.firebaseUser = firebaseUser;
        LiveData<List<Workmate>> dataBaseUsersLiveData = workmateRepository.getDataBaseUsers();
        workmatesViewStatesLiveData = bindViewState(dataBaseUsersLiveData);
    }

    @NonNull
    public LiveData<List<WorkmatesViewState>> getWorkmatesViewStatesLiveData() {
        return workmatesViewStatesLiveData;
    }

    @NonNull
    private LiveData<List<WorkmatesViewState>> bindViewState(LiveData<List<Workmate>> dataBaseUsersLiveData) {
        return Transformations.map(dataBaseUsersLiveData, workmates -> {
            List<WorkmatesViewState> viewStates = new ArrayList<>();

            for (Workmate workmate : workmates) {
                if (!workmate.getId().equals(firebaseUser.getUid())) {
                    String workmateName = (workmate.getSelectedRestaurant() == null) ? workmate.getName() + application.getString(R.string.no_selected_restaurant) : workmate.getName() + application.getString(R.string.a_restaurant_is_selected);
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
            return viewStates;
        });
    }
}


