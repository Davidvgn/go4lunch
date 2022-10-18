package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class GeometryResponse {

    @Nullable
    @SerializedName("viewport")
    private ViewportResponse viewportResponse;

    @Nullable
    @SerializedName("location")
    private LocationResponse locationResponse;

    public GeometryResponse(@Nullable ViewportResponse viewportResponse, @Nullable LocationResponse locationResponse) {
        this.viewportResponse = viewportResponse;
        this.locationResponse = locationResponse;
    }

    @Nullable
    public ViewportResponse getViewport() {
        return viewportResponse;
    }

    @Nullable
    public LocationResponse getLocation() {
        return locationResponse;
    }

    @NonNull
    @Override
    public String toString() {
        return
            "Geometry{" +
                "viewport = '" + viewportResponse + '\'' +
                ",location = '" + locationResponse + '\'' +
                "}";
    }
}