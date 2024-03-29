package com.davidvignon.go4lunch.data.google_places.autocomplete;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class TermsItem{

    @SerializedName("offset")
    private int offset;

    @SerializedName("value")
    private String value;

    public int getOffset(){
        return offset;
    }

    public String getValue(){
        return value;
    }
}