package com.davidvignon.go4lunch.data.google_places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.DataModule;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class NearBySearchRepository {

    @NonNull
    private final PlacesApi placesApi;

    @Inject
    public NearBySearchRepository(@NonNull @DataModule.PlacesAPi PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public LiveData<NearbySearchResponse> getNearbySearchResponse(double latitude, double longitude) {

        String nearBySearchApiKey = BuildConfig.NEARBY_API_KEY;
        MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

        placesApi.getNearbySearchResponse(
            latitude + "," + longitude,
            "1500",
            "restaurant",
            nearBySearchApiKey
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
}
