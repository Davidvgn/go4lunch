package com.davidvignon.go4lunch.data.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorkmateRepository {

    public static final String COLLECTION_PATH_USERS = "users";
    public static final String ID = "id";
    public static final String SELECTED_RESTAURANT = "selectedRestaurant";
    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public WorkmateRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<Workmate> getWorkmateInfoLiveData(String workmateId) {
        MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection(COLLECTION_PATH_USERS).whereEqualTo(ID, workmateId).addSnapshotListener((value, error) -> {
            if (value != null) {
                workmateMutableLiveData.setValue(value.toObjects(Workmate.class).get(0));
            }
        });
        return workmateMutableLiveData;
    }

    public LiveData<List<Workmate>> getDataBaseUsersLiveData() {
        MutableLiveData<List<Workmate>> workmateMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection(COLLECTION_PATH_USERS).addSnapshotListener((value, error) -> {
            if (value != null) {
                workmateMutableLiveData.setValue(value.toObjects(Workmate.class));
            }
        });
        return workmateMutableLiveData;
    }

    public LiveData<List<Workmate>> getUserListGoingToLiveData(String placeId) {
        MutableLiveData<List<Workmate>> userMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection(COLLECTION_PATH_USERS)
            .whereEqualTo(SELECTED_RESTAURANT, placeId)
            .addSnapshotListener((value, error) -> {
                if (value != null) {
                    userMutableLiveData.setValue(value.toObjects(Workmate.class));
                }
            });
        return userMutableLiveData;
    }

    public LiveData<Map<String, Integer>> getPlaceIdUserCountMapLiveData() {
        MutableLiveData<Map<String, Integer>> placeIdUserCountMapMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(COLLECTION_PATH_USERS)
            .addSnapshotListener((queryDocumentSnapshots, error) -> {
                if (queryDocumentSnapshots != null) {
                    Map<String, Integer> placeIdUserCountMap = new HashMap<>();

                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        String placeId = queryDocumentSnapshot.getString(SELECTED_RESTAURANT);

                        Integer previousCount = placeIdUserCountMap.get(placeId);

                        if (previousCount == null) {
                            previousCount = 0;
                        }

                        placeIdUserCountMap.put(placeId, previousCount + 1);
                    }

                    placeIdUserCountMapMutableLiveData.setValue(placeIdUserCountMap);
                }
            });

        return placeIdUserCountMapMutableLiveData;
    }
}
