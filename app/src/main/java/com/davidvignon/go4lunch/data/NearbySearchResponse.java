package com.davidvignon.go4lunch.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NearbySearchResponse {

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions;

    @SerializedName("results")
    private List<ResultsItem> results;

    @SerializedName("status")
    private String status;

    public String getNextPageToken(){
        return nextPageToken;
    }

    public List<Object> getHtmlAttributions(){
        return htmlAttributions;
    }

    public List<ResultsItem> getResults(){
        return results;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString() {
        return
            "NearbySearchResponse{" +
                "next_page_token = '" + nextPageToken + '\'' +
                ",results = '" + results + '\'' +
                ",status = '" + status + '\'' +
                "}";
    }
}