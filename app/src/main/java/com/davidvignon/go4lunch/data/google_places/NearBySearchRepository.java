package com.davidvignon.go4lunch.data.google_places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.DataModule;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class NearBySearchRepository {

    @NonNull
    private final PlacesApi placesApi;
    private final FirebaseFirestore firebaseFirestore;


    @Inject
    public NearBySearchRepository(@NonNull @DataModule.PlacesAPi PlacesApi placesApi, FirebaseFirestore firebaseFirestore) {
        this.placesApi = placesApi;
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<NearbySearchResponse> getNearbySearchResponse(double latitude, double longitude) {
        MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

        placesApi.getNearbySearchResponse(
            latitude + "," + longitude,
            "1500",
            "restaurant",
            "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8" //todo Nino on ne doit pas laisser la clé visible je suppose
        ).enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                nearbySearchResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {

            }
        });
        return nearbySearchResponseMutableLiveData;
    }

    public LiveData<Integer> howManyAreGoingThere(String placeId) {
        MutableLiveData<Integer> numberOfWorkmatesMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("users")
            .whereEqualTo("selectedRestaurant", placeId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                int count = queryDocumentSnapshots.size();
                numberOfWorkmatesMutableLiveData.setValue(count);
            })
            .addOnFailureListener(e -> {
                numberOfWorkmatesMutableLiveData.setValue(0);
            });

        return numberOfWorkmatesMutableLiveData;
    }



}
