package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class GeometryResponse {

    @SerializedName("viewport")
    private ViewportResponse viewportResponse;

    @SerializedName("location")
    private LocationResponse locationResponse;

    @Nullable
    public ViewportResponse getViewport() {
        return viewportResponse;
    }

    @Nullable
    public LocationResponse getLocation() {
        return locationResponse;
    }

    @Override
    public String toString() {
        return
            "Geometry{" +
                "viewport = '" + viewportResponse + '\'' +
                ",location = '" + locationResponse + '\'' +
                "}";
    }
}