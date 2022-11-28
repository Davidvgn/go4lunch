package com.davidvignon.go4lunch;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;


    @Inject
    public FirestoreRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<QuerySnapshot> getDataBaseUsers() {
        MutableLiveData<QuerySnapshot> workmatesMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("users")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("DavidVgn", "FirebaseRepo " + document.getId() + " => " + document.getData().get("name"));
                        workmatesMutableLiveData.setValue(task.getResult());
                        Log.d("DavidVgn", "onCompleteMutableLivedata: " + workmatesMutableLiveData.getValue());
                    }
                } else {
                    Log.d("DavidVgn", "Error getting documents: ", task.getException());
                }
            });

        return workmatesMutableLiveData;
    }
}

