package com.davidvignon.go4lunch.data.workmate;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

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
        firebaseFirestore.collection("users").whereEqualTo("id", workmateId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    workmateMutableLiveData.setValue(value.toObjects(Workmate.class).get(0));
                }
            }
        });
        return workmateMutableLiveData;
    }

    public LiveData<List<Workmate>> getDataBaseUsers() {
        MutableLiveData<List<Workmate>> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    workmateMutableLiveData.setValue(value.toObjects(Workmate.class));
                }
            }
        });
        return workmateMutableLiveData;
    }


    public LiveData<List<Workmate>> getUserListGoingTo(String placeId) {
        MutableLiveData<List<Workmate>> userMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").whereEqualTo("selectedRestaurant", placeId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    userMutableLiveData.setValue(value.toObjects(Workmate.class));
                }
            }
        });
        return userMutableLiveData;
    }

    public LiveData<String> getSelectedRestaurant() {
        MutableLiveData<String> selectedRestaurantMutableliveData = new MutableLiveData<>();

        firebaseFirestore
            .collection("users")
            .document()
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null) {
                        String selectedRestaurantField = documentSnapshot.getString("selectedRestaurant");
                        selectedRestaurantMutableliveData.setValue(selectedRestaurantField);
                    }
                }
            });
        return selectedRestaurantMutableliveData;
    }
}
