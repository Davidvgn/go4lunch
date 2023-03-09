package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class PhotosItem{

    @Nullable
    @SerializedName("photo_reference")
    private final String photoReference;

    @SerializedName("width")
    private int width;

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    @SerializedName("height")
    private int height;

    public PhotosItem(@Nullable String photoReference) {
        this.photoReference = photoReference;
    }

    @Nullable
    public String getPhotoReference(){
        return photoReference;
    }

    public int getWidth(){
        return width;
    }

    public List<String> getHtmlAttributions(){
        return htmlAttributions;
    }

    public int getHeight(){
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotosItem that = (PhotosItem) o;
        return width == that.width && height == that.height && Objects.equals(photoReference, that.photoReference) && Objects.equals(htmlAttributions, that.htmlAttributions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoReference, width, htmlAttributions, height);
    }

    @NonNull
    @Override
    public String toString() {
        return "PhotosItem{" +
            "photoReference='" + photoReference + '\'' +
            ", width=" + width +
            ", htmlAttributions=" + htmlAttributions +
            ", height=" + height +
            '}';
    }
}