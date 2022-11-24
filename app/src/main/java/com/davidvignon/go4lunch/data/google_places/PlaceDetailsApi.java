package com.davidvignon.go4lunch.data.google_places;

import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceDetailsApi {

    @GET("/maps/api/place/details/json")
    Call<DetailsResponse> getDetailsResponse(
        @Query("place_id") String placeId,
        @Query("key") String key
    );

}
