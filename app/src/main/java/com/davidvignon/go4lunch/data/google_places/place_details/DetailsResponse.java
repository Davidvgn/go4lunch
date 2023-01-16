package com.davidvignon.go4lunch.data.google_places.place_details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

public class DetailsResponse {

    @Nullable
    @SerializedName("result")
    private final RestaurantDetailsResponse result;

    @Nullable
    @SerializedName("html_attributions")
    private final List<Object> htmlAttributions;

    @Nullable
    @SerializedName("status")
    private final String status;

    public DetailsResponse(@Nullable RestaurantDetailsResponse result, @Nullable List<Object> htmlAttributions, @Nullable String status) {
        this.result = result;
        this.htmlAttributions = htmlAttributions;
        this.status = status;
    }

    @Nullable
    public RestaurantDetailsResponse getResult() {
        return result;
    }

    @Nullable
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    @Nullable
    public String getStatus() {
        return status;
    }
}