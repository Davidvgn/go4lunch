package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

//    private final LiveData<List<WorkmatesViewStates>> workmatesViewStates;

    @Inject
    public WorkmatesViewModel(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;

//        workmatesViewStates = bindViewState();
    }

//    @NonNull
//    public LiveData<List<WorkmatesViewStates>> getWorkmatesViewStatesLiveData() {
//        return workmatesViewStates;
//    }
//
//    private LiveData<List<WorkmatesViewStates>> bindViewState() {
//        List<WorkmatesViewStates> viewStates = new ArrayList<>();
//
//        viewStates.add(new WorkmatesViewStates("mail@mail.com", "John Doe"));
//
//        return viewStates;
//    }
}
