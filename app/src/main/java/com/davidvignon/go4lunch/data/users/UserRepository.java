package com.davidvignon.go4lunch.data.users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final FirebaseAuth firebaseAuth;


    @Inject
    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<Boolean> isRestaurantSelectedLiveData(String placeId) {
        MutableLiveData<Boolean> isSelectedLiveData = new MutableLiveData<>();
        firebaseFirestore
            .collection("users")
            .document(firebaseAuth.getCurrentUser().getUid())
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null) {
                        String selectedRestaurantField = documentSnapshot.getString("selectedRestaurant");
                        if (Objects.equals(selectedRestaurantField, placeId)) {
                            isSelectedLiveData.setValue(true);
                        } else {
                            isSelectedLiveData.setValue(false);
                        }
                    } else {
                        isSelectedLiveData.setValue(false);
                    }
                }
            });
        return isSelectedLiveData;
    }

    public void toggleRestaurantSelected(String placeId, String restaurantName) {
        DocumentReference documentReference = firebaseFirestore
            .collection("users")
            .document(firebaseAuth.getCurrentUser().getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String selectedRestaurantField = task.getResult().getString("selectedRestaurant");
                if (!Objects.equals(selectedRestaurantField, placeId)) {
                    documentReference.update("selectedRestaurant", (placeId))
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DavidVgn", "DocumentSnapshot for placeId : " + placeId + " successfully updated!");
                        })
                        .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
                    documentReference.update("selectedRestaurantName", (restaurantName))
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DavidVgn", "DocumentSnapshot for placeId : " + restaurantName + " successfully updated!");
                        })
                        .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
                } else {
                    documentReference.update("selectedRestaurant", null)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DavidVgn", "DocumentSnapshot for placeId : " + placeId + " successfully updated!");
                        })
                        .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
                    documentReference.update("selectedRestaurantName", (null))
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DavidVgn", "DocumentSnapshot for placeId : " + restaurantName + " successfully updated!");
                        })
                        .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
                }
            }
        });
    }

    public LiveData<Boolean> isRestaurantLikedByUserLiveData(String placeId) {
        MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
        firebaseFirestore.collection("users")
            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null) {
                        List<String> firestoreList = (List<String>) documentSnapshot.get("favoritesRestaurants");
                        if(firestoreList != null) {
                            if (firestoreList.contains(placeId)) {
                                isLiked.setValue(true);
                            } else {
                                isLiked.setValue(false);
                            }
                        }
                    } else {
                        isLiked.setValue(false);
                    }
                }
            });
        return isLiked;
    }

    public void toggleRestaurantLiked(String placeId) {
        DocumentReference documentReference = firebaseFirestore
            .collection("users")
            .document(firebaseAuth.getCurrentUser().getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                List<String> favoritesRestaurantsField = (List<String>) documentSnapshot.get("favoritesRestaurants");

                if (!favoritesRestaurantsField.contains(placeId)) {
                    documentReference.update("favoritesRestaurants", FieldValue.arrayUnion(placeId))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("DavidVgn", "DocumentSnapshot successfully updated!");
                            }
                        });
                } else {
                    documentReference.update("favoritesRestaurants", FieldValue.arrayRemove(placeId))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("DavidVgn", "DocumentSnapshot successfully updated! : element removed");
                            }
                        });
                }
            }
        });
    }
}

