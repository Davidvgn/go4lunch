package com.davidvignon.go4lunch.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorkmatesRepository {


    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @Inject
    public WorkmatesRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;

    }

    public LiveData<List<Workmates>> getDataBaseUsers() {
        MutableLiveData<List<Workmates>> userMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("users")
            .get().
            addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userMutableLiveData.setValue(task.getResult().toObjects(Workmates.class));
                } else {
                    Log.d("DavidVgn", "Error getting documents: ");
                }
            });
        return userMutableLiveData;
    }
}
