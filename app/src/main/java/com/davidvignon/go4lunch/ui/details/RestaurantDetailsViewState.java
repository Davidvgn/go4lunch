package com.davidvignon.go4lunch.ui.details;

import androidx.annotation.NonNull;

import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;

import java.util.Objects;

public class RestaurantDetailsViewState {

    @NonNull
    private final String name;

    @NonNull
    private final String vicinity;

    @NonNull
    private final String phoneNumber;

    @NonNull
    private final String website;

    @NonNull
    private final String photosItemResponse;

    private final float rating;

    private final boolean isSelected;
    private final boolean isLiked;

    public RestaurantDetailsViewState(
        @NonNull String name,
        @NonNull String vicinity,
        @NonNull String phoneNumber,
        @NonNull String website,
        @NonNull String photosItemResponse,
        float rating,
        boolean isSelected,
        boolean isLiked
        ) {
        this.name = name;
        this.vicinity = vicinity;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.photosItemResponse = photosItemResponse;
        this.rating = rating;
        this.isSelected = isSelected;
        this.isLiked = isLiked;
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
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getWebsite() {
        return website;
    }

    @NonNull
    public String getPhotosItemResponse() {
        return photosItemResponse;
    }

    public float getRating() {
        return rating;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsViewState that = (RestaurantDetailsViewState) o;
        return Double.compare(that.rating, rating) == 0 && name.equals(that.name) && vicinity.equals(that.vicinity) && phoneNumber.equals(that.phoneNumber) && website.equals(that.website) && photosItemResponse.equals(that.photosItemResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vicinity, phoneNumber, website, photosItemResponse, rating);
    }

    @Override
    public String toString() {
        return "RestaurantDetailsViewState{" +
            "name='" + name + '\'' +
            ", vicinity='" + vicinity + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", website='" + website + '\'' +
            ", photosItemResponse=" + photosItemResponse +
            ", rating=" + rating +
            '}';
    }
}
