package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class EditorialSummary{

    @SerializedName("overview")
    private String overview;

    @SerializedName("language")
    private String language;

    public String getOverview(){
        return overview;
    }

    public String getLanguage(){
        return language;
    }
}