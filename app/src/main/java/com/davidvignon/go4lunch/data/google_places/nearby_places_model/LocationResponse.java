package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import javax.annotation.Nullable;

public class LocationResponse {

    @Nullable
    @SerializedName("lng")
    private final Double lng;

    @Nullable
    @SerializedName("lat")
    private final Double lat;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationResponse that = (LocationResponse) o;
        return Objects.equals(lng, that.lng) && Objects.equals(lat, that.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lng, lat);
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