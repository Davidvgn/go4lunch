package com.davidvignon.go4lunch.data.google_places.autocomplete;

import com.google.gson.annotations.SerializedName;

public class MainTextMatchedSubstringsItem{

    @SerializedName("offset")
    private int offset;

    @SerializedName("length")
    private int length;

    public int getOffset(){
        return offset;
    }

    public int getLength(){
        return length;
    }
}