package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class NortheastResponse {

    @Nullable
    @SerializedName("lng")
    private Double lng;

    @Nullable
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

    @NonNull
    @Override
    public String toString() {
        return
            "Northeast{" +
                "lng = '" + lng + '\'' +
                ",lat = '" + lat + '\'' +
                "}";
    }
}