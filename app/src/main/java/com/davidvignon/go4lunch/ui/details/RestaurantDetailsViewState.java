package com.davidvignon.go4lunch.ui.details;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public class RestaurantDetailsViewState {

    @NonNull
    private final String name;

    @NonNull
    private final String vicinity;

    @NonNull
    private final String phoneNumber;

    @NonNull
    private final String website;

    @NonNull
    private final String photoUrl;

    private final float rating;

    private final boolean isSelected;

    private final String likeButtonText;

    @DrawableRes
    private final int likeButtonIcon;

    public RestaurantDetailsViewState(
        @NonNull String name,
        @NonNull String vicinity,
        @NonNull String phoneNumber,
        @NonNull String website,
        @NonNull String photoUrl,
        float rating,
        boolean isSelected,
        String likeButtonText,
        int likeButtonIcon
    ) {
        this.name = name;
        this.vicinity = vicinity;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.isSelected = isSelected;
        this.likeButtonText = likeButtonText;
        this.likeButtonIcon = likeButtonIcon;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getWebsite() {
        return website;
    }

    @NonNull
    public String getPhotoUrl() {
        return photoUrl;
    }

    public float getRating() {
        return rating;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getLikeButtonText() {
        return likeButtonText;
    }

    public int getLikeButtonIcon() {
        return likeButtonIcon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsViewState that = (RestaurantDetailsViewState) o;
        return Float.compare(that.rating, rating) == 0 && isSelected == that.isSelected && likeButtonIcon == that.likeButtonIcon && name.equals(that.name) && vicinity.equals(that.vicinity) && phoneNumber.equals(that.phoneNumber) && website.equals(that.website) && photoUrl.equals(that.photoUrl) && Objects.equals(likeButtonText, that.likeButtonText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vicinity, phoneNumber, website, photoUrl, rating, isSelected, likeButtonText, likeButtonIcon);
    }

    @NonNull
    @Override
    public String toString() {
        return "RestaurantDetailsViewState{" +
            "name='" + name + '\'' +
            ", vicinity='" + vicinity + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", website='" + website + '\'' +
            ", photoUrl='" + photoUrl + '\'' +
            ", rating=" + rating +
            ", isSelected=" + isSelected +
            ", likeButtonText='" + likeButtonText + '\'' +
            ", likeButtonIcon=" + likeButtonIcon +
            '}';
    }
}
