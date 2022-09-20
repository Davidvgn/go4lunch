package com.davidvignon.go4lunch.data;

import com.google.gson.annotations.SerializedName;

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

    @Override
    public String toString() {
        return
            "Geometry{" +
                "viewport = '" + viewport + '\'' +
                ",location = '" + location + '\'' +
                "}";
    }
}