package com.davidvignon.go4lunch.data;

import com.google.gson.annotations.SerializedName;

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

    @Override
    public String toString() {
        return
            "Viewport{" +
                "southwest = '" + southwest + '\'' +
                ",northeast = '" + northeast + '\'' +
                "}";
    }
}