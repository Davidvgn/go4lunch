package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import javax.annotation.Nullable;

@Keep
public class GeometryResponse {

    @Nullable
    @SerializedName("viewport")
    private final ViewportResponse viewportResponse;

    @Nullable
    @SerializedName("location")
    private final LocationResponse locationResponse;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeometryResponse that = (GeometryResponse) o;
        return Objects.equals(viewportResponse, that.viewportResponse) && Objects.equals(locationResponse, that.locationResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewportResponse, locationResponse);
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