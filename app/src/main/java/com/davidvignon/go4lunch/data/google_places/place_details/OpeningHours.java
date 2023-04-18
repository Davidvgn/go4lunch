package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@Keep
public class OpeningHours{

    @SerializedName("open_now")
    private boolean openNow;

    @SerializedName("periods")
    private List<PeriodsItem> periods;

    @SerializedName("weekday_text")
    private List<String> weekdayText;

    public boolean isOpenNow(){
        return openNow;
    }

    public List<PeriodsItem> getPeriods(){
        return periods;
    }

    public List<String> getWeekdayText(){
        return weekdayText;
    }
}