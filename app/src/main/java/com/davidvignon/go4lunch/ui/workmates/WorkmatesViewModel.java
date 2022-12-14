package com.davidvignon.go4lunch.ui.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.oauth.OAuthActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData;

    private final UserRepository userRepository;

    @Inject
    public WorkmatesViewModel(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;

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

                Log.d("DavidVgn", "apply: "+ result.get(0).toString());

                if (result != null) {
//                    for (int i = 0; i < result.size(); i++) {
//                        if (result.get(i).getId().equals()){
//                            result.remove(i);
//                            break;
//                        }
//                    }
                    for (User user : result) {
                        viewStates.add(new WorkmatesViewStates(
                            user.getId(),
                            user.getName(),
                            user.getPicturePath()));
                    }
                }
                return viewStates;

            }
        });
    }
}


