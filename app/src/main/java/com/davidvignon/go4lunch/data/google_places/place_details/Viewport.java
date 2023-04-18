package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Viewport{

    @SerializedName("southwest")
    private Southwest southwest;

    @SerializedName("northeast")
    private Northeast northeast;

    public Southwest getSouthwest(){
        return southwest;
    }

    public Northeast getNortheast(){
        return northeast;
    }
}