package com.davidvignon.go4lunch.data.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorkmateRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public WorkmateRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<Workmate> getWorkmateInfoLiveData(String workmateId) {
        MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").whereEqualTo("id", workmateId).addSnapshotListener((value, error) -> {
            if (value != null) {
                workmateMutableLiveData.setValue(value.toObjects(Workmate.class).get(0));
            }
        });
        return workmateMutableLiveData;
    }

    public LiveData<List<Workmate>> getDataBaseUsersLiveData() {
        MutableLiveData<List<Workmate>> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").addSnapshotListener((value, error) -> {
            if (value != null) {
                workmateMutableLiveData.setValue(value.toObjects(Workmate.class));
            }
        });
        return workmateMutableLiveData;
    }


    public LiveData<List<Workmate>> getUserListGoingToLiveData(String placeId) {
        MutableLiveData<List<Workmate>> userMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").whereEqualTo("selectedRestaurant", placeId).addSnapshotListener((value, error) -> {
            if (value != null) {
                userMutableLiveData.setValue(value.toObjects(Workmate.class));
            }
        });
        return userMutableLiveData;
    }
    public LiveData<Integer> howManyAreGoingThere(String placeId) {
        MutableLiveData<Integer> numberOfWorkmatesMutableLiveData = new MutableLiveData<>();


        firebaseFirestore.collection("users")
            .whereEqualTo("selectedRestaurant", placeId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                int count = queryDocumentSnapshots.size();
                numberOfWorkmatesMutableLiveData.setValue(count);
            })
            .addOnFailureListener(e -> {
                numberOfWorkmatesMutableLiveData.setValue(0);
            });

        return numberOfWorkmatesMutableLiveData;
    }
}
