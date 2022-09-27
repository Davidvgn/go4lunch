package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

public class NearbySearchResponse {

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions;

    @SerializedName("results")
    private List<RestaurantResponse> results;

    @SerializedName("status")
    private String status;

    @Nullable
    public String getNextPageToken() {
        return nextPageToken;
    }

    @Nullable
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    @Nullable
    public List<RestaurantResponse> getResults() {
        return results;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
            "NearbySearchResponse{" +
                "next_page_token = '" + nextPageToken + '\'' +
                ",results = '" + results + '\'' +
                ",status = '" + status + '\'' +
                "}";
    }
}