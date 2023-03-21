package com.davidvignon.go4lunch.data.google_places.autocomplete;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class PredictionsResponse {

    @SerializedName("predictions")
    private final List<PredictionsItem> predictions;

    @SerializedName("status")
    private final String status;

    public PredictionsResponse(List<PredictionsItem> predictions, String status) {
        this.predictions = predictions;
        this.status = status;
    }

    public List<PredictionsItem> getPredictions(){
        return predictions;
    }

    public String getStatus(){
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

    @Override
    public String toString() {
        return "AutoCompleteResponse{" +
            "predictions=" + predictions +
            ", status='" + status + '\'' +
            '}';
    }
}