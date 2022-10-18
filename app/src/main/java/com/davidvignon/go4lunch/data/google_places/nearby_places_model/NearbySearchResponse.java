package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

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

    public NearbySearchResponse(String nextPageToken, List<Object> htmlAttributions, List<RestaurantResponse> results, String status) {
        this.nextPageToken = nextPageToken;
        this.htmlAttributions = htmlAttributions;
        this.results = results;
        this.status = status;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearbySearchResponse that = (NearbySearchResponse) o;
        return Objects.equals(nextPageToken, that.nextPageToken) && Objects.equals(htmlAttributions, that.htmlAttributions) && Objects.equals(results, that.results) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextPageToken, htmlAttributions, results, status);
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