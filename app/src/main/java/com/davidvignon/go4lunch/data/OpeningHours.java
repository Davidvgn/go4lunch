package com.davidvignon.go4lunch.data;

import com.google.gson.annotations.SerializedName;

public class OpeningHours{

    @SerializedName("open_now")
    private boolean openNow;

    public boolean isOpenNow(){
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