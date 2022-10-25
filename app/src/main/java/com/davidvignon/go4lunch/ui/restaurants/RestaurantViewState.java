package com.davidvignon.go4lunch.ui.restaurants;

import androidx.annotation.NonNull;

import java.util.Objects;

import javax.annotation.Nullable;

public class RestaurantViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final Boolean permanentlyClosed;

    @NonNull
    private final String name;

    @NonNull
    private final String vicinity;


    public RestaurantViewState(@NonNull String placeId,
        @NonNull Boolean permanentlyClosed,
        @NonNull String name,
        @NonNull String vincinity) {
        this.placeId = placeId;
        this.permanentlyClosed = permanentlyClosed;
        this.name = name;
        this.vicinity = vincinity;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public Boolean getPermanentlyClosed() {
        return permanentlyClosed;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantViewState that = (RestaurantViewState) o;
        return placeId.equals(that.getPlaceId())
            && name.equals((that.getName()))
            && permanentlyClosed.equals(that.getPermanentlyClosed())
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
