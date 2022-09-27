package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

public class PhotosItemResponse {

    @SerializedName("photo_reference")
    private String photoReference;

    @SerializedName("width")
    private int width;

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    @SerializedName("height")
    private int height;

    @Nullable
    public String getPhotoReference() {
        return photoReference;
    }

    @Nullable
    public int getWidth() {
        return width;
    }

    @Nullable
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    @Nullable
    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return
            "PhotosItem{" +
                "photo_reference = '" + photoReference + '\'' +
                ",width = '" + width + '\'' +
                ",html_attributions = '" + htmlAttributions + '\'' +
                ",height = '" + height + '\'' +
                "}";
    }
}