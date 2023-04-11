package com.davidvignon.go4lunch.data.google_places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PlaceDetailsRepository {


    @NonNull
    private final PlacesApi placesApi;

    @Inject
    public PlaceDetailsRepository(@NonNull PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public LiveData<DetailsResponse> getDetailsResponseLiveData(@NonNull String placeId) {
        String API_KEY = BuildConfig.NEARBY_API_KEY;
        MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();

        placesApi.getDetailsResponse(placeId,
            API_KEY).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<DetailsResponse> call, @NonNull Response<DetailsResponse> response) {
                detailsResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DetailsResponse> call, @NonNull Throwable t) {
            }
        });
        return detailsResponseMutableLiveData;
    }
}
