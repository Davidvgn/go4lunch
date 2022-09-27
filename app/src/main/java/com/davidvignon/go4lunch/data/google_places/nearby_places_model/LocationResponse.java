package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class LocationResponse {

    @SerializedName("lng")
    private double lng;

    @SerializedName("lat")
    private double lat;

    @Nullable
    public double getLng() {
        return lng;
    }

    @Nullable
    public double getLat() {
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