package com.davidvignon.go4lunch.data.workmate;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorkmateRepository {


    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final FirebaseAuth firebaseAuth;


    @Inject
    public WorkmateRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseAuth = firebaseAuth;

    }

    public LiveData<Workmate> getWorkmateInfo(String workmateId) {
        MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").whereEqualTo("id", workmateId).addSnapshotListener((value, error) -> {
            if (value != null) {
                workmateMutableLiveData.setValue(value.toObjects(Workmate.class).get(0));
            }
        });
        return workmateMutableLiveData;
    }

    public LiveData<List<Workmate>> getDataBaseUsers() {
        MutableLiveData<List<Workmate>> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").addSnapshotListener((value, error) -> {
            if (value != null) {
                workmateMutableLiveData.setValue(value.toObjects(Workmate.class));
            }
        });
        return workmateMutableLiveData;
    }


    public LiveData<List<Workmate>> getUserListGoingTo(String placeId) {
        MutableLiveData<List<Workmate>> userMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").whereEqualTo("selectedRestaurant", placeId).addSnapshotListener((value, error) -> {
            if (value != null) {
                userMutableLiveData.setValue(value.toObjects(Workmate.class));
            }
        });
        return userMutableLiveData;
    }

//    public LiveData<String> getSelectedRestaurant() {
//        MutableLiveData<String> selectedRestaurantMutableliveData = new MutableLiveData<>();
//
//        firebaseFirestore
//            .collection("users")
//            .document()
//            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                    if (documentSnapshot != null) {
//                        String selectedRestaurantField = documentSnapshot.getString("selectedRestaurant");
//                        selectedRestaurantMutableliveData.setValue(selectedRestaurantField);
//                    }
//                }
//            });
//        return selectedRestaurantMutableliveData;
//    }
}
