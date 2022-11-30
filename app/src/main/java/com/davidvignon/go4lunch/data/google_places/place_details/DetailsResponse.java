package com.davidvignon.go4lunch.data.google_places.place_details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DetailsResponse{

    @SerializedName("result")
    private RestaurantDetailsResponse result;

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions;

    @SerializedName("status")
    private String status;

    public DetailsResponse(RestaurantDetailsResponse result, List<Object> htmlAttributions, String status) {
        this.result = result;
        this.htmlAttributions = htmlAttributions;
        this.status = status;
    }

    public RestaurantDetailsResponse getResult(){
        return result;
    }

    public List<Object> getHtmlAttributions(){
        return htmlAttributions;
    }

    public String getStatus(){
        return status;
    }
}