package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

@Keep
public class OpeningHoursResponse {

    @Nullable
    @SerializedName("open_now")
    private Boolean openNow;

    @Nullable
    public Boolean isOpenNow() {
        return openNow;
    }

    public OpeningHoursResponse() {
    }

    @VisibleForTesting
    public OpeningHoursResponse(@Nullable Boolean openNow) {
        this.openNow = openNow;
    }

    @NonNull
    @Override
    public String toString() {
        return
            "OpeningHours{" +
                "open_now = '" + openNow + '\'' +
                "}";
    }
}