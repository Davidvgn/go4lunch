package com.davidvignon.go4lunch.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    public LiveData<String> getRestaurantPlaceIdLiveData() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MutableLiveData<String> selectedRestaurantFieldMutable = new MutableLiveData<>();
        firebaseFirestore.collection("users")
            .document((FirebaseAuth.getInstance().getCurrentUser()).getUid())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String selectedRestaurantField = document.getString("selectedRestaurant");
                        selectedRestaurantFieldMutable.setValue(selectedRestaurantField);
                    } else {
                        selectedRestaurantFieldMutable.setValue(null);
                    }
                } else {
                    selectedRestaurantFieldMutable.setValue(null);
                }
                countDownLatch.countDown();

            });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return selectedRestaurantFieldMutable;
    }

    public LiveData<List<Workmate>> getUserListGoingToLiveData(String placeId) {
        MutableLiveData<List<Workmate>> userMutableLiveData = new MutableLiveData<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        firebaseFirestore.collection("users").whereEqualTo("selectedRestaurant", placeId).addSnapshotListener((value, error) -> {
            if (value != null ) {
                userMutableLiveData.setValue(value.toObjects(Workmate.class));
            }
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userMutableLiveData;
    }
}
