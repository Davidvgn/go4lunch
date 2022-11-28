package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.FirestoreRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewStates>> workmatesViewStatesLiveData;

    @Inject
    public WorkmatesViewModel(@NonNull FirestoreRepository firestoreRepository) {

        LiveData<QuerySnapshot> dataBaseUsersLiveData = firestoreRepository.getDataBaseUsers();
        workmatesViewStatesLiveData = bindViewState(dataBaseUsersLiveData);
    }

    @NonNull
    public LiveData<List<WorkmatesViewStates>> getWorkmatesViewStatesLiveData(){
        return workmatesViewStatesLiveData;
    }

    @NonNull
    private LiveData<List<WorkmatesViewStates>> bindViewState(LiveData<QuerySnapshot> dataBaseUsersLiveData) {
        return Transformations.map(dataBaseUsersLiveData, result -> {
            List<WorkmatesViewStates> viewStates = new ArrayList<>();

            for (DocumentSnapshot documentSnapshot : result.getDocuments()) {

                viewStates.add(new WorkmatesViewStates(
                    documentSnapshot.getString("name"),
                    documentSnapshot.getString("picturePath")
                    ));
            }
            return viewStates;
        });
    }
}


