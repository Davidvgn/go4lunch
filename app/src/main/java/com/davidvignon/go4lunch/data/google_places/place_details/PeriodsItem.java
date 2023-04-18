package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class PeriodsItem{

    @SerializedName("close")
    private Close close;

    @SerializedName("open")
    private Open open;

    public Close getClose(){
        return close;
    }

    public Open getOpen(){
        return open;
    }
}