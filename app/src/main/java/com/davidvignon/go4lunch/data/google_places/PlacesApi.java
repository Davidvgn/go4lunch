package com.davidvignon.go4lunch.data.google_places;

import com.davidvignon.go4lunch.data.google_places.autocomplete.PredictionsResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.NearbySearchResponse;
import com.davidvignon.go4lunch.data.google_places.place_details.DetailsResponse;

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

    @GET("maps/api/place/details/json")
    Call<DetailsResponse> getDetailsResponse(
        @Query("place_id") String placeId,
        @Query("key") String key
    );

    @GET("maps/api/place/autocomplete/json")
    Call<PredictionsResponse> getPredictionsResponse(
        @Query("input") String input,
        @Query("location") String location,
        @Query("radius") String radius,
        @Query("types") String types,
        @Query("key") String key
    );
}
