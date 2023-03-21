package com.davidvignon.go4lunch.ui.main.predictions;

import androidx.annotation.NonNull;

import java.util.Objects;

public class PredictionViewState {

    @NonNull
    private final String placeId;
    private final String description;

    public PredictionViewState(@NonNull String placeId, String description) {
        this.placeId = placeId;
        this.description = description;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictionViewState that = (PredictionViewState) o;
        return placeId.equals(that.placeId) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, description);
    }

    @Override
    public String toString() {
        return "PredictionViewState{" +
            "placeId='" + placeId + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
