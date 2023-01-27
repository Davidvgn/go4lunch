package com.davidvignon.go4lunch.data.users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
            .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())//todo Nino le requireNonNull c'est ok ?
            .addSnapshotListener((documentSnapshot, error) -> {
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
            });
        return isSelectedLiveData;
    }

    public void toggleRestaurantSelected(String placeId, String restaurantName) {
        DocumentReference documentReference = firebaseFirestore
            .collection("users")
            .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

        documentReference.get().addOnCompleteListener(task -> {
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
        });
    }

    public LiveData<Boolean> isRestaurantLikedByUserLiveData(String placeId) {
        MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
        firebaseFirestore.collection("users")
            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .addSnapshotListener((documentSnapshot, error) -> {
                if (documentSnapshot != null) {
                    List<String> firestoreList = (List<String>) documentSnapshot.get("favoritesRestaurants");
                    if (firestoreList != null) {
                        if (firestoreList.contains(placeId)) {
                            isLiked.setValue(true);
                        } else {
                            isLiked.setValue(false);
                        }
                    }
                } else {
                    isLiked.setValue(false);
                }
            });
        return isLiked;
    }

    public void toggleRestaurantLiked(String placeId) {
        DocumentReference documentReference = firebaseFirestore
            .collection("users")
            .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

        documentReference.get().addOnCompleteListener(task -> {
            DocumentSnapshot documentSnapshot = task.getResult();
            List<String> favoritesRestaurantsField = (List<String>) documentSnapshot.get("favoritesRestaurants");

            if (!favoritesRestaurantsField.contains(placeId)) {
                documentReference.update("favoritesRestaurants", FieldValue.arrayUnion(placeId))
                    .addOnSuccessListener(aVoid -> Log.d("DavidVgn", "DocumentSnapshot successfully updated!"));
            } else {
                documentReference.update("favoritesRestaurants", FieldValue.arrayRemove(placeId))
                    .addOnSuccessListener(aVoid -> Log.d("DavidVgn", "DocumentSnapshot successfully updated! : element removed"));
            }
        });
    }

    public LiveData<String> getRestaurantPlaceId() {
        MutableLiveData<String> selectedRestaurantFieldMutable = new MutableLiveData<>();
        firebaseFirestore.collection("users")
            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .addSnapshotListener((documentSnapshot, error) -> {
                if (documentSnapshot != null) {
                    if (documentSnapshot.get("selectedRestaurant") != null) {
                        String firestoreList = documentSnapshot.get("selectedRestaurant").toString();
                        selectedRestaurantFieldMutable.setValue(firestoreList);
                    } else{
                        selectedRestaurantFieldMutable.setValue(null);
                    }
                }
            });
        return selectedRestaurantFieldMutable;
    }
}

