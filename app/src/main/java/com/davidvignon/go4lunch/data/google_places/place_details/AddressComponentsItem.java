package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@Keep
public class AddressComponentsItem{

    @SerializedName("types")
    private List<String> types;

    @SerializedName("short_name")
    private String shortName;

    @SerializedName("long_name")
    private String longName;

    public List<String> getTypes(){
        return types;
    }

    public String getShortName(){
        return shortName;
    }

    public String getLongName(){
        return longName;
    }
}