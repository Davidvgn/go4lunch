package com.davidvignon.go4lunch.data.users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<List<User>> getDataBaseUsers() {
        MutableLiveData<List<User>> workmatesMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("users")
            .get().
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        workmatesMutableLiveData.setValue(task.getResult().toObjects(User.class));
                    } else {
                        Log.d("DavidVgn", "Error getting documents: ");
                    }
                }
            });

        return workmatesMutableLiveData;
    }
}

