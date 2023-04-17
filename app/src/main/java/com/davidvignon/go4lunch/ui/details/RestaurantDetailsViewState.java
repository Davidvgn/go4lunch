package com.davidvignon.go4lunch.ui.details;

import androidx.annotation.ColorRes;
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

    @DrawableRes
    private final int selectedIconDrawableRes;

    @ColorRes
    private final int selectedBackgroundColorRes;

    @NonNull
    private final String likeButtonText;

    @DrawableRes
    private final int likeButtonIconDrawableRes;

    public RestaurantDetailsViewState(
        @NonNull String name,
        @NonNull String vicinity,
        @NonNull String phoneNumber,
        @NonNull String website,
        @NonNull String photoUrl,
        float rating,
        @DrawableRes int selectedIconDrawableRes,
        @ColorRes int selectedBackgroundColorRes,
        @NonNull String likeButtonText,
        @DrawableRes int likeButtonIconDrawableRes
    ) {
        this.name = name;
        this.vicinity = vicinity;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.selectedIconDrawableRes = selectedIconDrawableRes;
        this.selectedBackgroundColorRes = selectedBackgroundColorRes;
        this.likeButtonText = likeButtonText;
        this.likeButtonIconDrawableRes = likeButtonIconDrawableRes;
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

    @DrawableRes
    public int getSelectedIconDrawableRes() {
        return selectedIconDrawableRes;
    }

    @ColorRes
    public int getSelectedBackgroundColorRes() {
        return selectedBackgroundColorRes;
    }

    @NonNull
    public String getLikeButtonText() {
        return likeButtonText;
    }

    @DrawableRes
    public int getLikeButtonIconDrawableRes() {
        return likeButtonIconDrawableRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsViewState that = (RestaurantDetailsViewState) o;
        return Float.compare(that.rating, rating) == 0 && selectedIconDrawableRes == that.selectedIconDrawableRes && selectedBackgroundColorRes == that.selectedBackgroundColorRes && likeButtonIconDrawableRes == that.likeButtonIconDrawableRes && name.equals(that.name) && vicinity.equals(that.vicinity) && phoneNumber.equals(that.phoneNumber) && website.equals(that.website) && photoUrl.equals(that.photoUrl) && likeButtonText.equals(that.likeButtonText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vicinity, phoneNumber, website, photoUrl, rating, selectedIconDrawableRes, selectedBackgroundColorRes, likeButtonText, likeButtonIconDrawableRes);
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
            ", selectedIconDrawableRes=" + selectedIconDrawableRes +
            ", selectedBackgroundColorRes=" + selectedBackgroundColorRes +
            ", likeButtonText='" + likeButtonText + '\'' +
            ", likeButtonIconDrawableRes=" + likeButtonIconDrawableRes +
            '}';
    }
}
