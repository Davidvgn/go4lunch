package com.davidvignon.go4lunch.ui.map;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MapPoiViewState {
    @NonNull
    private final String placeId;
    @NonNull
    private final String title;
    private final double latitude;
    private final double longitude;
    private final float hue;

    public MapPoiViewState(@NonNull String placeId, @NonNull String title, double latitude, double longitude, float hue) {
        this.placeId = placeId;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hue = hue;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getHue() {
        return hue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapPoiViewState that = (MapPoiViewState) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Float.compare(that.hue, hue) == 0 && placeId.equals(that.placeId) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, title, latitude, longitude, hue);
    }

    @NonNull
    @Override
    public String toString() {
        return "MapPoiViewState{" +
            "placeId='" + placeId + '\'' +
            ", title='" + title + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", hue=" + hue +
            '}';
    }
}
