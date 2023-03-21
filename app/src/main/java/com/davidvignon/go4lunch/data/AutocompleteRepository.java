package com.davidvignon.go4lunch.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AutocompleteRepository {

    private static final String RADIUS_METERS = "1500";
    private static final String TYPE = "restaurant";

    @NonNull
    private final PlacesApi placesApi;

    @Inject
    public AutocompleteRepository(@NonNull PlacesApi placesApi) {
        this.placesApi = placesApi;
    }


    public LiveData<PredictionsResponse> getPredictionsResponse(double latitude, double longitude, String input) {

        MutableLiveData<PredictionsResponse> predictionsResponseMutableLiveData = new MutableLiveData<>();

        placesApi.getPredictionsResponse(
            input,
            latitude + "," + longitude,
            RADIUS_METERS,
            TYPE,
            BuildConfig.AUTOCOMPLETE_API_KEY
        ).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PredictionsResponse> call, @NonNull Response<PredictionsResponse> response) {
                predictionsResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PredictionsResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return predictionsResponseMutableLiveData;

    }
}
