package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class PlusCode{

    @SerializedName("compound_code")
    private String compoundCode;

    @SerializedName("global_code")
    private String globalCode;

    public String getCompoundCode(){
        return compoundCode;
    }

    public String getGlobalCode(){
        return globalCode;
    }
}