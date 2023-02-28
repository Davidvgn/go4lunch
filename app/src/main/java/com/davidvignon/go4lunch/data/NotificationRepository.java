package com.davidvignon.go4lunch.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationRepository {
    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public NotificationRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    @WorkerThread
    public String getRestaurantName() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String[] selectedRestaurant = new String[1];
        firebaseFirestore.collection("users")
            .document((FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        selectedRestaurant[0] = document.getString("selectedRestaurantName");
                    }
                }
                countDownLatch.countDown();
            });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return selectedRestaurant[0];
    }

    @WorkerThread
    public String getRestaurantPlaceId() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String[] selectedRestaurantField = new String[1];
        firebaseFirestore.collection("users")
            .document((FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        selectedRestaurantField[0] = document.getString("selectedRestaurant");
                    }
                }
                countDownLatch.countDown();
            });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return selectedRestaurantField[0];
    }


    @WorkerThread
    public List<Workmate> getUserListWill(String placeId) {
        List<Workmate> workmateArrayList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        firebaseFirestore.collection("users").whereEqualTo("selectedRestaurant", placeId).addSnapshotListener((value, error) -> {
            if (value != null ) {
                workmateArrayList.addAll(value.toObjects(Workmate.class));
            }
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return workmateArrayList;
    }
}
