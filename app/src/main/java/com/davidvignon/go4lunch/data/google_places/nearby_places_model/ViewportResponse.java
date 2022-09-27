package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ViewportResponse {

    @SerializedName("southwest")
    private SouthwestResponse southwestResponse;

    @SerializedName("northeast")
    private NortheastResponse northeastResponse;

    @Nullable
    public SouthwestResponse getSouthwest() {
        return southwestResponse;
    }

    @Nullable
    public NortheastResponse getNortheast() {
        return northeastResponse;
    }

    @Override
    public String toString() {
        return
            "Viewport{" +
                "southwest = '" + southwestResponse + '\'' +
                ",northeast = '" + northeastResponse + '\'' +
                "}";
    }
}