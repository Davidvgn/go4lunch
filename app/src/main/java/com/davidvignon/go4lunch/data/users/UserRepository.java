package com.davidvignon.go4lunch.data.users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final FirebaseAuth firebaseAuth;

    public MutableLiveData<Boolean> isSelected = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
    public String selectedRestaurantField;

    @Inject
    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<Boolean> selectRestaurant(String placeId) {
            DocumentReference user = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());

            user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    selectedRestaurantField = documentSnapshot.getString("selectedRestaurant");
                }
            });

            if (!Objects.equals(selectedRestaurantField, placeId)) {
                user.update("selectedRestaurant", (placeId))
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DavidVgn", "DocumentSnapshot successfully updated!");
                        isSelected.setValue(true);
                    })
                    .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
            } else {
                user.update("selectedRestaurant", null)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DavidVgn", "DocumentSnapshot successfully updated!");
                        isSelected.setValue(false);
                    })
                    .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
            }
            return isSelected;
        }

    public LiveData<Boolean> likeRestaurant(String placeId) {
        DocumentReference user = firebaseFirestore.collection("users").document(firebaseAuth.getUid());
        user.update("favoritesRestaurants", FieldValue.arrayUnion(placeId))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("DavidVgn", "DocumentSnapshot successfully updated!");
                    isLiked.setValue(true);
                }
            })
            .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document" + firebaseAuth.getCurrentUser().getUid(), e));
        return isLiked;
    }


    public LiveData<Boolean> unLikeRestaurant(String placeId) {
        DocumentReference user = firebaseFirestore.collection("users").document(firebaseAuth.getUid());

        user.update("favoritesRestaurants", FieldValue.arrayRemove(placeId))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("DavidVgn", "DocumentSnapshot successfully updated!");
                    Log.d("DavidVgn", "isremoved");
                    isLiked.setValue(false);
                }
            })
            .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
        return isLiked;
    }


}

