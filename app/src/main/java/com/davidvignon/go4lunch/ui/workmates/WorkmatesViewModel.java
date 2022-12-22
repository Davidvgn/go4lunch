package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData;

    @NonNull
    private final FirebaseUser firebaseUser;

    @Inject
    public WorkmatesViewModel(UserRepository userRepository,@NonNull FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        LiveData<List<User>> dataBaseUsersLiveData = userRepository.getDataBaseUsers();
        workmatesViewStatesLiveData = bindViewState(dataBaseUsersLiveData);
    }

    @NonNull
    public LiveData<List<WorkmatesViewStates>> getWorkmatesViewStatesLiveData() {
        return workmatesViewStatesLiveData;
    }

    @NonNull
    private LiveData<List<WorkmatesViewStates>> bindViewState(LiveData<List<User>> dataBaseUsersLiveData) {
        return Transformations.map(dataBaseUsersLiveData, new Function<List<User>, List<WorkmatesViewStates>>() {
            @Override
            public List<WorkmatesViewStates> apply(List<User> result) {
                List<WorkmatesViewStates> viewStates = new ArrayList<>();

                for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).getId().equals(firebaseUser.getUid())){
                            result.remove(i);
                            break;
                        }
                    }
                    for (User user : result) {
                        viewStates.add(new WorkmatesViewStates(
                            user.getId(),
                            user.getName(),
                            user.getPicturePath()));
                    }

                return viewStates;

            }
        });
    }
}


