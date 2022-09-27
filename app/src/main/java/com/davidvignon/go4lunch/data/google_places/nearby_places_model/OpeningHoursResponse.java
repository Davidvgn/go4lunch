package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class OpeningHoursResponse {

    @SerializedName("open_now")
    private boolean openNow;

    @Nullable
    public boolean isOpenNow() {
        return openNow;
    }

    @Override
    public String toString() {
        return
            "OpeningHours{" +
                "open_now = '" + openNow + '\'' +
                "}";
    }
}