package com.davidvignon.go4lunch.ui.restaurants;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.Objects;

import javax.annotation.Nullable;

public class RestaurantViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final String name;

    @NonNull
    private final String vicinity;

    @NonNull
    private final String photosItemResponse;

    @StringRes
    private final int openOrClose;

    private final float rating;

    public RestaurantViewState(
        @NonNull String placeId,
        @NonNull String name,
        @NonNull String vincinity,
        @NonNull String photosItemResponse,
        @StringRes int openOrClose,
        float rating
    ) {
        this.placeId = placeId;
        this.name = name;
        this.vicinity = vincinity;
        this.photosItemResponse = photosItemResponse;
        this.openOrClose = openOrClose;
        this.rating = rating;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @NonNull
    public String getPhotosItemResponse() {
        return photosItemResponse;
    }

    @StringRes
    public int getOpenOrClose() {
        return openOrClose;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantViewState that = (RestaurantViewState) o;
        return openOrClose == that.openOrClose && Float.compare(that.rating, rating) == 0 && placeId.equals(that.placeId) && name.equals(that.name) && vicinity.equals(that.vicinity) && photosItemResponse.equals(that.photosItemResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, name, vicinity, photosItemResponse, openOrClose, rating);
    }

    @Override
    public String toString() {
        return "RestaurantViewState{" +
            "placeId='" + placeId + '\'' +
            ", name='" + name + '\'' +
            ", vicinity='" + vicinity + '\'' +
            ", photosItemResponse='" + photosItemResponse + '\'' +
            ", openOrClose=" + openOrClose +
            ", rating=" + rating +
            '}';
    }
}
