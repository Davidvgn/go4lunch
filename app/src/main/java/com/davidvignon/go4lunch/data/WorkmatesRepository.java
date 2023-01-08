package com.davidvignon.go4lunch.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.ui.workmates.WorkmatesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorkmatesRepository {


    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final FirebaseAuth firebaseAuth;


    @Inject
    public WorkmatesRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseAuth = firebaseAuth;

    }
    //todo David voir avec NINO
//    public LiveData<List<Workmates>> getAllWorkmatesLiveData() {
//        MutableLiveData<List<Workmates>> workmatesMutableLiveData = new MutableLiveData<>();
//
//        firebaseFirestore.collection("users")
//            .get().
//            addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    List<Workmates> completeList = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Workmates workmate = document.toObject(Workmates.class);
//                        // Check if the workmate is not the current user
//                        if (!workmate.getId().equals(firebaseAuth.getCurrentUser().getUid())) {
//                            completeList.add(workmate);
//                        }
//                    }
//                    workmatesMutableLiveData.setValue(completeList);
//                } else {
//                    Log.d("DavidVgn", "Error getting documents: ");
//                }
//            });
//        return workmatesMutableLiveData;
//    }


    public LiveData<List<Workmates>> getDataBaseUsers() {
        MutableLiveData<List<Workmates>> userMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    userMutableLiveData.setValue(value.toObjects(Workmates.class));
                }
            }
        });
        return userMutableLiveData;
    }
}
