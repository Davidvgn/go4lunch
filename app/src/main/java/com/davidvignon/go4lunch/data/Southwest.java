package com.davidvignon.go4lunch.data;

import com.google.gson.annotations.SerializedName;

public class Southwest{

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

    @Override
    public String toString() {
        return
            "Southwest{" +
                "lng = '" + lng + '\'' +
                ",lat = '" + lat + '\'' +
                "}";
    }
}