package com.davidvignon.go4lunch.data.google_places.place_details;

import com.google.gson.annotations.SerializedName;

public class ReviewsItem{

    @SerializedName("author_name")
    private String authorName;

    @SerializedName("profile_photo_url")
    private String profilePhotoUrl;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("author_url")
    private String authorUrl;

    @SerializedName("rating")
    private int rating;

    @SerializedName("language")
    private String language;

    @SerializedName("text")
    private String text;

    @SerializedName("time")
    private int time;

    @SerializedName("translated")
    private boolean translated;

    @SerializedName("relative_time_description")
    private String relativeTimeDescription;

    public String getAuthorName(){
        return authorName;
    }

    public String getProfilePhotoUrl(){
        return profilePhotoUrl;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public String getAuthorUrl(){
        return authorUrl;
    }

    public int getRating(){
        return rating;
    }

    public String getLanguage(){
        return language;
    }

    public String getText(){
        return text;
    }

    public int getTime(){
        return time;
    }

    public boolean isTranslated(){
        return translated;
    }

    public String getRelativeTimeDescription(){
        return relativeTimeDescription;
    }
}