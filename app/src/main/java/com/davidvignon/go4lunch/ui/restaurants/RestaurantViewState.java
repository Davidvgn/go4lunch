package com.davidvignon.go4lunch.ui.restaurants;

import androidx.annotation.NonNull;

import com.davidvignon.go4lunch.data.google_places.nearby_places_model.OpeningHoursResponse;
import com.davidvignon.go4lunch.data.google_places.nearby_places_model.PhotosItemResponse;

import java.util.List;
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

    @NonNull
    private final String openOrClose;
    private float rating;



    public RestaurantViewState(@NonNull String placeId,
        @NonNull String name,
        @NonNull String vincinity,
        @NonNull String photosItemResponse,
        @NonNull String openOrClose,
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

    @NonNull
    public String getOpenOrClose() {
        return openOrClose;
    }

    @NonNull
    public float getRating() {
        return rating;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantViewState that = (RestaurantViewState) o;
        return placeId.equals(that.getPlaceId())
            && name.equals((that.getName()))
            && vicinity.equals(that.getVicinity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, name);
    }

    @NonNull
    @Override
    public String toString() {
        return "RestaurantViewState{" +
            "placeId=" + placeId +
            ", name='" + name + '\'' +
            ", vicinity='" + vicinity + '\'' +
            '}';
    }
}
