package com.davidvignon.go4lunch.data.google_places.autocomplete;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class PredictionsResponse {

    @Nullable
    @SerializedName("predictions")
    private final List<PredictionsItem> predictions;

    @Nullable
    @SerializedName("status")
    private final String status;

    public PredictionsResponse(@Nullable
    List<PredictionsItem> predictions, @Nullable
    String status) {
        this.predictions = predictions;
        this.status = status;
    }

    @Nullable
    public List<PredictionsItem> getPredictions() {
        return predictions;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictionsResponse that = (PredictionsResponse) o;
        return Objects.equals(predictions, that.predictions) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predictions, status);
    }

    @NonNull
    @Override
    public String toString() {
        return "AutoCompleteResponse{" +
            "predictions=" + predictions +
            ", status='" + status + '\'' +
            '}';
    }
}