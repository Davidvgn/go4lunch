package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class LocationResponse {

    @SerializedName("lng")
    private Double lng;

    @SerializedName("lat")
    private Double lat;

    @Nullable
    public Double getLng() {
        return lng;
    }

    @Nullable
    public Double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return
            "Location{" +
                "lng = '" + lng + '\'' +
                ",lat = '" + lat + '\'' +
                "}";
    }
}