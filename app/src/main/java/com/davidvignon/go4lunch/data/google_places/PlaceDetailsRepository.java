package com.davidvignon.go4lunch.data.google_places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PlaceDetailsRepository {

    @NonNull
    PlaceDetailsApi placeDetailsApi;

    @Inject
    public PlaceDetailsRepository(@NonNull PlaceDetailsApi placeDetailsApi) {
        this.placeDetailsApi = placeDetailsApi;
    }

   public LiveData<DetailsResponse> getDetailsResponse(@NonNull String placeId) {
        MutableLiveData<DetailsResponse> detailsResponseMutableLiveData = new MutableLiveData<>();

        placeDetailsApi.getDetailsResponse(placeId,
            "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8").enqueue(new Callback<DetailsResponse>() {
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
