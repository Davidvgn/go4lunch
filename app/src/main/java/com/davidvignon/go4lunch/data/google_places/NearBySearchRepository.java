package com.davidvignon.go4lunch.data.google_places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class NearBySearchRepository {

    private static final String RADIUS_METERS = "1500";
    private static final String TYPE = "restaurant";

    @NonNull
    private final PlacesApi placesApi;

    @Inject
    public NearBySearchRepository(@NonNull PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public LiveData<NearbySearchResponse> getNearbySearchResponse(double latitude, double longitude) {

        MutableLiveData<NearbySearchResponse> nearbySearchResponseMutableLiveData = new MutableLiveData<>();

        placesApi.getNearbySearchResponse(
            latitude + "," + longitude,
            RADIUS_METERS,
            TYPE,
            BuildConfig.NEARBY_API_KEY
        ).enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                nearbySearchResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return nearbySearchResponseMutableLiveData;
    }
}
