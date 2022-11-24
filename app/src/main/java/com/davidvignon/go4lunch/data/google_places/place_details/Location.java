package com.davidvignon.go4lunch.data.google_places.place_details;

import com.google.gson.annotations.SerializedName;

public class Location{

    @SerializedName("lng")
    private double lng;

    @SerializedName("lat")
    private double lat;

    public double getLng(){
        return lng;
    }

    public double getLat(){
        return lat;
    }
}