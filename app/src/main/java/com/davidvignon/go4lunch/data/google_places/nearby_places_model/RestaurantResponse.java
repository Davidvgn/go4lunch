package com.davidvignon.go4lunch.data.google_places.nearby_places_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

public class RestaurantResponse {

    @Nullable
    @SerializedName("types")
    private final List<String> types;

    @Nullable
    @SerializedName("business_status")
    private final String businessStatus;

    @Nullable
    @SerializedName("icon")
    private final String icon;

    @Nullable
    @SerializedName("rating")
    private final Double rating;

    @Nullable
    @SerializedName("icon_background_color")
    private final String iconBackgroundColor;

    @Nullable
    @SerializedName("photos")
    private final List<PhotosItemResponse> photos;

    @Nullable
    @SerializedName("reference")
    private final String reference;

    @Nullable
    @SerializedName("user_ratings_total")
    private final Integer userRatingsTotal;

    @Nullable
    @SerializedName("price_level")
    private final Integer priceLevel;

    @Nullable
    @SerializedName("scope")
    private final String scope;

    @Nullable
    @SerializedName("name")
    private final String name;

    @Nullable
    @SerializedName("opening_hours")
    private final OpeningHoursResponse openingHoursResponse;

    @Nullable
    @SerializedName("geometry")
    private final GeometryResponse geometryResponse;

    @Nullable
    @SerializedName("icon_mask_base_uri")
    private final String iconMaskBaseUri;

    @Nullable
    @SerializedName("vicinity")
    private final String vicinity;

    @Nullable
    @SerializedName("plus_code")
    private final PlusCodeResponse plusCodeResponse;

    @Nullable
    @SerializedName("place_id")
    private final String placeId;

    @Nullable
    @SerializedName("permanently_closed")
    private final Boolean permanentlyClosed;

    public RestaurantResponse(
        @Nullable List<String> types,
        @Nullable String businessStatus,
        @Nullable String icon,
        @Nullable Double rating,
        @Nullable String iconBackgroundColor,
        @Nullable List<PhotosItemResponse> photos,
        @Nullable String reference,
        @Nullable Integer userRatingsTotal,
        @Nullable Integer priceLevel,
        @Nullable String scope,
        @Nullable String name,
        @Nullable OpeningHoursResponse openingHoursResponse,
        @Nullable GeometryResponse geometryResponse,
        @Nullable String iconMaskBaseUri,
        @Nullable String vicinity,
        @Nullable PlusCodeResponse plusCodeResponse,
        @Nullable String placeId,
        @Nullable Boolean permanentlyClosed
    ) {
        this.types = types;
        this.businessStatus = businessStatus;
        this.icon = icon;
        this.rating = rating;
        this.iconBackgroundColor = iconBackgroundColor;
        this.photos = photos;
        this.reference = reference;
        this.userRatingsTotal = userRatingsTotal;
        this.priceLevel = priceLevel;
        this.scope = scope;
        this.name = name;
        this.openingHoursResponse = openingHoursResponse;
        this.geometryResponse = geometryResponse;
        this.iconMaskBaseUri = iconMaskBaseUri;
        this.vicinity = vicinity;
        this.plusCodeResponse = plusCodeResponse;
        this.placeId = placeId;
        this.permanentlyClosed = permanentlyClosed;
    }

    @Nullable
    public List<String> getTypes() {
        return types;
    }

    @Nullable
    public String getBusinessStatus() {
        return businessStatus;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    @Nullable
    public Double getRating() {
        return rating;
    }

    @Nullable
    public String getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    @Nullable
    public List<PhotosItemResponse> getPhotos() {
        return photos;
    }

    @Nullable
    public String getReference() {
        return reference;
    }

    @Nullable
    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    @Nullable
    public Integer getPriceLevel() {
        return priceLevel;
    }

    @Nullable
    public String getScope() {
        return scope;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public OpeningHoursResponse getOpeningHours() {
        return openingHoursResponse;
    }

    @Nullable
    public GeometryResponse getGeometry() {
        return geometryResponse;
    }

    @Nullable
    public String getIconMaskBaseUri() {
        return iconMaskBaseUri;
    }

    @Nullable
    public String getVicinity() {
        return vicinity;
    }

    @Nullable
    public PlusCodeResponse getPlusCode() {
        return plusCodeResponse;
    }

    @Nullable
    public String getPlaceId() {
        return placeId;
    }

    public Boolean isPermanentlyClosed() {
        return permanentlyClosed;
    }

    @NonNull
    @Override
    public String toString() {
        return
            "ResultsItem{" +
                "types = '" + types + '\'' +
                ",business_status = '" + businessStatus + '\'' +
                ",icon = '" + icon + '\'' +
                ",rating = '" + rating + '\'' +
                ",icon_background_color = '" + iconBackgroundColor + '\'' +
                ",photos = '" + photos + '\'' +
                ",reference = '" + reference + '\'' +
                ",user_ratings_total = '" + userRatingsTotal + '\'' +
                ",price_level = '" + priceLevel + '\'' +
                ",scope = '" + scope + '\'' +
                ",name = '" + name + '\'' +
                ",opening_hours = '" + openingHoursResponse + '\'' +
                ",geometry = '" + geometryResponse + '\'' +
                ",icon_mask_base_uri = '" + iconMaskBaseUri + '\'' +
                ",vicinity = '" + vicinity + '\'' +
                ",plus_code = '" + plusCodeResponse + '\'' +
                ",place_id = '" + placeId + '\'' +
                ",permanently_closed = '" + permanentlyClosed + '\'' +
                "}";
    }
}