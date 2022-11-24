package com.davidvignon.go4lunch.data.google_places.place_details;

import com.google.gson.annotations.SerializedName;

public class Open{

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("day")
    private int day;

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public int getDay(){
        return day;
    }
}