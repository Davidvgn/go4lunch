package com.davidvignon.go4lunch.ui.map;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MapPoiViewState {
    private final String placeId;
    private final String title;
    private final double latitude;
    private final double longitude;

    public MapPoiViewState(String placeId, String title, double latitude, double longitude) {
        this.placeId = placeId;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapPoiViewState that = (MapPoiViewState) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Objects.equals(placeId, that.placeId) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, title, latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "MapPoiViewState{" +
            "placeId='" + placeId + '\'' +
            ", title='" + title + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
