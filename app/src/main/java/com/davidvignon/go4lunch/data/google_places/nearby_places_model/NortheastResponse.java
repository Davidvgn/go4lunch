package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

public class NortheastResponse {

    @SerializedName("lng")
    private double lng;

    @SerializedName("lat")
    private double lat;

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return
            "Northeast{" +
                "lng = '" + lng + '\'' +
                ",lat = '" + lat + '\'' +
                "}";
    }
}