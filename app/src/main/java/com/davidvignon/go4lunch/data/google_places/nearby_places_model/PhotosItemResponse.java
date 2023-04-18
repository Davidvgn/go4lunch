package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

@Keep
public class PhotosItemResponse {

    @SerializedName("photo_reference")
    private final String photoReference;

    @SerializedName("width")
    private int width;

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    @SerializedName("height")
    private int height;

    public PhotosItemResponse(String photoReference) {
        this.photoReference = photoReference;
    }

    @Nullable
    public String getPhotoReference() {
        return photoReference;
    }

    public int getWidth() {
        return width;
    }

    @Nullable
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public int getHeight() {
        return height;
    }

    @NonNull
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