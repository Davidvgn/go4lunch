package com.davidvignon.go4lunch.data.users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final FirebaseUser firebaseUser;
    @NonNull
    private final FirebaseAuth firebaseAuth;

    @Inject
    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseUser firebaseUser, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<List<User>> getDataBaseUsers() {
        MutableLiveData<List<User>> userMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("users")
            .get().
            addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userMutableLiveData.setValue(task.getResult().toObjects(User.class));
                } else {
                    Log.d("DavidVgn", "Error getting documents: ");
                }
            });
        return userMutableLiveData;
    }

    public void likeRestaurant(String placeId) {
        DocumentReference user = firebaseFirestore.collection("users").document(firebaseUser.getDisplayName());//todo david trouver un autre moyen que name car homonyme possible
        user
            .update("favoritesRestaurants", FieldValue.arrayUnion(placeId))
            .addOnSuccessListener(aVoid -> Log.d("DavidVgn", "DocumentSnapshot successfully updated!"))
            .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document" + firebaseUser.getDisplayName(), e));
    }

    public void unLikeRestaurant(String placeId) {
        DocumentReference user = firebaseFirestore.collection("users").document(firebaseUser.getDisplayName());//todo david trouver un autre moyen que name car homonyme possible

        user
            .update("favoritesRestaurants", FieldValue.arrayRemove(placeId))
            .addOnSuccessListener(aVoid -> Log.d("DavidVgn", "DocumentSnapshot successfully updated!"))
            .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
    }

    public void selectRestaurant(String placeId) {
//        DocumentReference user = firebaseFirestore.collection("users").document(firebaseUser.getDisplayName());//todo david trouver un autre moyen que name car homonyme possible
        DocumentReference user = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());//todo david trouver un autre moyen que name car homonyme possible
                user
                    .update("selectedRestaurant", (placeId))
                    .addOnSuccessListener(aVoid -> Log.d("DavidVgn", "DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));

    }

    public void unSelectRestaurant() {
//        DocumentReference user = firebaseFirestore.collection("users").document(firebaseUser.getDisplayName());//todo david trouver un autre moyen que name car homonyme possible
        DocumentReference user = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());//todo david trouver un autre moyen que name car homonyme possible
        user
            .update("selectedRestaurant", null)
            .addOnSuccessListener(aVoid -> Log.d("DavidVgn", "DocumentSnapshot successfully updated!"))
            .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
    }

    public LiveData<List<String>>  getUserSelectedRestaurant(){
        MutableLiveData <List<String>> test =new MutableLiveData<>();

        firebaseFirestore.collection("users")
            .document(firebaseUser.getDisplayName())
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<String> list = new ArrayList<>();
                        list.add(document.getString("selectedRestaurant"));

                        test.setValue(list);
                        Log.d("DavidVgn", "Task is Successful!" + test.getValue());

                        if (document.exists()) {
                            Log.d("DavidVgn", "Document exists!");
                        } else {
                            Log.d("DavidVgn", "Document does not exist!");
                        }
                    } else {
                        Log.d("DavidVgn", "Failed with: ", task.getException());
                    }
                }
            });
        Log.d("DavidVgn", "value userRepo!" + test.getValue());

        return test;
    }

}

