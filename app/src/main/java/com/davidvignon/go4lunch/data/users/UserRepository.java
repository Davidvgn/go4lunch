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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    public static final String COLLECTION_PATH_USERS = "users";
    public static final String SELECTED_RESTAURANT = "selectedRestaurant";
    public static final String SELECTED_RESTAURANT_NAME = "selectedRestaurantName";
    public static final String FAVORITES_RESTAURANTS = "favoritesRestaurants";
    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final FirebaseAuth firebaseAuth;

    @Inject
    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseAuth = firebaseAuth;
    }


    public LiveData<User> getUserLiveData() {
        MutableLiveData<User> documentSnapshotMutableLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseFirestore
                .collection(COLLECTION_PATH_USERS)
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        documentSnapshotMutableLiveData.setValue(task.getResult().toObject(User.class));
                    } else {
                        Log.e("UserRepository", "get failed with ", task.getException());
                    }
                });
        }
        return documentSnapshotMutableLiveData;
    }

    public LiveData<Boolean> isRestaurantSelectedLiveData(String placeId) {
        MutableLiveData<Boolean> isSelectedLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseFirestore
                .collection(COLLECTION_PATH_USERS)
                .document(firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (documentSnapshot != null) {
                        String selectedRestaurantField = documentSnapshot.getString(SELECTED_RESTAURANT);
                        if (Objects.equals(selectedRestaurantField, placeId)) {
                            isSelectedLiveData.setValue(true);
                        } else {
                            isSelectedLiveData.setValue(false);
                        }
                    } else {
                        isSelectedLiveData.setValue(false);
                    }
                });
        }
        return isSelectedLiveData;
    }

    public void toggleRestaurantSelected(String placeId, String restaurantName) {
        if (firebaseAuth.getCurrentUser() != null) {
            DocumentReference documentReference = firebaseFirestore
                .collection(COLLECTION_PATH_USERS)
                .document((firebaseAuth.getCurrentUser()).getUid());

            documentReference.get().addOnCompleteListener(task -> {
                String selectedRestaurantField = task.getResult().getString(SELECTED_RESTAURANT);
                if (!Objects.equals(selectedRestaurantField, placeId)) {
                    documentReference.update(SELECTED_RESTAURANT, (placeId))
                        .addOnSuccessListener(aVoid -> Log.i("UserRepository", "DocumentSnapshot for placeId : " + placeId + " successfully updated!"))
                        .addOnFailureListener(e -> Log.w("UserRepository", "Error updating document", e));
                    documentReference.update(SELECTED_RESTAURANT_NAME, (restaurantName))
                        .addOnSuccessListener(aVoid -> Log.i("UserRepository", "DocumentSnapshot for placeId : " + restaurantName + " successfully updated!"))
                        .addOnFailureListener(e -> Log.w("UserRepository", "Error updating document", e));
                } else {
                    documentReference.update(SELECTED_RESTAURANT, null)
                        .addOnSuccessListener(aVoid -> Log.i("UserRepository", "DocumentSnapshot for placeId : " + placeId + " successfully updated!"))
                        .addOnFailureListener(e -> Log.w("UserRepository", "Error updating document", e));
                    documentReference.update(SELECTED_RESTAURANT_NAME, (null))
                        .addOnSuccessListener(aVoid -> Log.i("UserRepository", "DocumentSnapshot for placeId : " + restaurantName + " successfully updated!"))
                        .addOnFailureListener(e -> Log.w("DavidVgn", "Error updating document", e));
                }
            });
        }
    }

    public LiveData<Boolean> isRestaurantLikedByUserLiveData(String placeId) {
        MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseFirestore.collection(COLLECTION_PATH_USERS)
                .document((FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            List<String> firestoreList = user.getFavoritesRestaurants();
                            isLiked.setValue(firestoreList != null && firestoreList.contains(placeId));
                        } else {
                            isLiked.setValue(false);
                        }
                    } else {
                        isLiked.setValue(false);
                    }
                });
        }
        return isLiked;
    }

    public void toggleRestaurantLiked(String placeId) {
        if (firebaseAuth.getCurrentUser() != null) {
            DocumentReference documentReference = firebaseFirestore
                .collection(COLLECTION_PATH_USERS)
                .document((firebaseAuth.getCurrentUser()).getUid());

            documentReference.get().addOnCompleteListener(task -> {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        List<String> favoritesRestaurantsField = user.getFavoritesRestaurants();
                        if (favoritesRestaurantsField == null) {
                            favoritesRestaurantsField = new ArrayList<>();
                        }
                        if (!favoritesRestaurantsField.contains(placeId)) {
                            documentReference.update(FAVORITES_RESTAURANTS, FieldValue.arrayUnion(placeId))
                                .addOnSuccessListener(aVoid -> Log.i("UserRepository", "DocumentSnapshot successfully updated!"));
                        } else {
                            documentReference.update(FAVORITES_RESTAURANTS, FieldValue.arrayRemove(placeId))
                                .addOnSuccessListener(aVoid -> Log.i("UserRepository", "DocumentSnapshot successfully updated! : element removed"));
                        }
                    }
                }
            });
        }
    }

    public LiveData<String> getRestaurantPlaceIdLiveData() {
        MutableLiveData<String> selectedRestaurantFieldMutable = new MutableLiveData<>();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseFirestore.collection(COLLECTION_PATH_USERS)
                .document((FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            String selectedRestaurantId = user.getSelectedRestaurant();
                            selectedRestaurantFieldMutable.setValue(selectedRestaurantId);
                        } else {
                            selectedRestaurantFieldMutable.setValue(null);
                        }
                    }
                });
        }
        return selectedRestaurantFieldMutable;
    }

}

