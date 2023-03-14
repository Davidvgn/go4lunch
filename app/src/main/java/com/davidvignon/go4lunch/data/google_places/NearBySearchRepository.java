package com.davidvignon.go4lunch.data.google_places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.BuildConfig;
import com.davidvignon.go4lunch.data.DataModule;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;
import java.util.List;

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
    private final PlacesClient placesClient;

    @Inject
    public NearBySearchRepository(@NonNull @DataModule.PlacesAPi PlacesApi placesApi, PlacesClient placesClient) {
        this.placesApi = placesApi;
        this.placesClient = placesClient;
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

    public LiveData<List<AutocompletePrediction>> getAutocompletePredictionListLiveData(double latitude, double longitude, String query) {

        MutableLiveData<List<AutocompletePrediction>> searchValueMutableLiveData = new MutableLiveData<>();

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        RectangularBounds bounds = RectangularBounds.newInstance(
            new LatLng(latitude -0.01, longitude -0.01),
            new LatLng(latitude +0.01, longitude +0.01)
        );

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(bounds)
            .setCountries("FR")
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setSessionToken(token)
            .setQuery(query)
            .build();

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                @Override
                public void onSuccess(FindAutocompletePredictionsResponse response) {
                    searchValueMutableLiveData.setValue(response.getAutocompletePredictions());
                    Log.d("Dvgn", "getAuto: " + response.getAutocompletePredictions());
                }
            })
            .addOnFailureListener(e -> searchValueMutableLiveData.setValue(Collections.emptyList()));
        return searchValueMutableLiveData;
    }
}
