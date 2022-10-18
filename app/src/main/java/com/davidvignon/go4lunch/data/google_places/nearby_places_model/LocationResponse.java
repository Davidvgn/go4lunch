package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class LocationResponse {

    @Nullable
    @SerializedName("lng")
    private Double lng;

    @Nullable
    @SerializedName("lat")
    private Double lat;

    public LocationResponse(@Nullable Double lng, @Nullable Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

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
            "Location{" +
                "lng = '" + lng + '\'' +
                ",lat = '" + lat + '\'' +
                "}";
    }
}