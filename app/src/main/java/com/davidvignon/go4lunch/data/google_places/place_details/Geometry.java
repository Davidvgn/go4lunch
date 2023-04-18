package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Geometry{

    @SerializedName("viewport")
    private Viewport viewport;

    @SerializedName("location")
    private Location location;

    public Viewport getViewport(){
        return viewport;
    }

    public Location getLocation(){
        return location;
    }
}