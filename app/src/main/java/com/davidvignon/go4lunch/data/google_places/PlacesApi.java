package com.davidvignon.go4lunch.data.google_places;

import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbySearchResponse> getNearbySearchResponse(
        @Query("location") String location,
        @Query("radius") String radius,
        @Query("type") String type,
        @Query("key") String key
    );
}
