package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.data.users.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData;

    @Inject
    public WorkmatesViewModel(@NonNull UserRepository userRepository) {

        LiveData<List<User>> dataBaseUsersLiveData = userRepository.getDataBaseUsers();
        workmatesViewStatesLiveData = bindViewState(dataBaseUsersLiveData);
    }

    @NonNull
    public LiveData<List<WorkmatesViewStates>> getWorkmatesViewStatesLiveData() {
        return workmatesViewStatesLiveData;
    }

    @NonNull
    private LiveData<List<WorkmatesViewStates>> bindViewState(LiveData<List<User>> dataBaseUsersLiveData) {
        return Transformations.map(dataBaseUsersLiveData, result -> {
            List<WorkmatesViewStates> viewStates = new ArrayList<>();

            for (User user : result) {

                viewStates.add(new WorkmatesViewStates(
                    user.getName(),
                    user.getPicturePath()));
            }
            return viewStates;
        });
    }
}


