package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class PlusCodeResponse {

    @SerializedName("compound_code")
    private String compoundCode;

    @SerializedName("global_code")
    private String globalCode;

    @Nullable
    public String getCompoundCode() {
        return compoundCode;
    }

    @Nullable
    public String getGlobalCode() {
        return globalCode;
    }

    @Override
    public String toString() {
        return
            "PlusCode{" +
                "compound_code = '" + compoundCode + '\'' +
                ",global_code = '" + globalCode + '\'' +
                "}";
    }
}