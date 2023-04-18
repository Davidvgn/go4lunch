package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Keep
public class RestaurantDetailsResponse {

    @Nullable
    @SerializedName("utc_offset")
    private Integer utcOffset;

    @Nullable
    @SerializedName("formatted_address")
    private String formattedAddress;

    @Nullable
    @SerializedName("wheelchair_accessible_entrance")
    private Boolean wheelchairAccessibleEntrance;

    @Nullable
    @SerializedName("reservable")
    private Boolean reservable;

    @Nullable
    @SerializedName("icon")
    private String icon;

    @Nullable
    @SerializedName("rating")
    private Double rating;

    @Nullable
    @SerializedName("icon_background_color")
    private String iconBackgroundColor;

    @Nullable
    @SerializedName("takeout")
    private Boolean takeout;

    @Nullable
    @SerializedName("photos")
    private List<PhotosItem> photos;

    @Nullable
    @SerializedName("editorial_summary")
    private EditorialSummary editorialSummary;

    @Nullable
    @SerializedName("reference")
    private String reference;

    @Nullable
    @SerializedName("dine_in")
    private Boolean dineIn;

    @Nullable
    @SerializedName("current_opening_hours")
    private CurrentOpeningHours currentOpeningHours;

    @Nullable
    @SerializedName("user_ratings_total")
    private Integer userRatingsTotal;

    @Nullable
    @SerializedName("reviews")
    private List<ReviewsItem> reviews;

    @Nullable
    @SerializedName("serves_dinner")
    private Boolean servesDinner;

    @Nullable
    @SerializedName("icon_mask_base_uri")
    private String iconMaskBaseUri;

    @Nullable
    @SerializedName("serves_brunch")
    private Boolean servesBrunch;

    @Nullable
    @SerializedName("serves_breakfast")
    private Boolean servesBreakfast;

    @Nullable
    @SerializedName("serves_wine")
    private Boolean servesWine;

    @Nullable
    @SerializedName("adr_address")
    private String adrAddress;

    @Nullable
    @SerializedName("place_id")
    private String placeId;

    @Nullable
    @SerializedName("serves_lunch")
    private Boolean servesLunch;

    @Nullable
    @SerializedName("delivery")
    private Boolean delivery;

    @Nullable
    @SerializedName("types")
    private List<String> types;

    @Nullable
    @SerializedName("website")
    private String website;

    @Nullable
    @SerializedName("business_status")
    private String businessStatus;

    @Nullable
    @SerializedName("address_components")
    private List<AddressComponentsItem> addressComponents;

    @Nullable
    @SerializedName("url")
    private String url;

    @Nullable
    @SerializedName("price_level")
    private Integer priceLevel;

    @Nullable
    @SerializedName("name")
    private String name;

    @Nullable
    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @Nullable
    @SerializedName("geometry")
    private Geometry geometry;

    @Nullable
    @SerializedName("vicinity")
    private String vicinity;

    @Nullable
    @SerializedName("plus_code")
    private PlusCode plusCode;

    @Nullable
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @Nullable
    @SerializedName("international_phone_number")
    private String internationalPhoneNumber;

    @Nullable
    @SerializedName("serves_beer")
    private Boolean servesBeer;

    @Nullable
    public Integer getUtcOffset() {
        return utcOffset;
    }

    @Nullable
    public String getFormattedAddress() {
        return formattedAddress;
    }

    @Nullable
    public Boolean isWheelchairAccessibleEntrance() {
        return wheelchairAccessibleEntrance;
    }

    @Nullable
    public Boolean isReservable() {
        return reservable;
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
    public Boolean isTakeout() {
        return takeout;
    }

    @Nullable
    public List<PhotosItem> getPhotos() {
        return photos;
    }

    @Nullable
    public EditorialSummary getEditorialSummary() {
        return editorialSummary;
    }

    @Nullable
    public String getReference() {
        return reference;
    }

    @Nullable
    public Boolean isDineIn() {
        return dineIn;
    }

    @Nullable
    public CurrentOpeningHours getCurrentOpeningHours() {
        return currentOpeningHours;
    }

    @Nullable
    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    @Nullable
    public List<ReviewsItem> getReviews() {
        return reviews;
    }

    @Nullable
    public Boolean isServesDinner() {
        return servesDinner;
    }

    @Nullable
    public String getIconMaskBaseUri() {
        return iconMaskBaseUri;
    }

    @Nullable
    public Boolean isServesBrunch() {
        return servesBrunch;
    }

    @Nullable
    public Boolean isServesBreakfast() {
        return servesBreakfast;
    }

    @Nullable
    public Boolean isServesWine() {
        return servesWine;
    }

    @Nullable
    public String getAdrAddress() {
        return adrAddress;
    }

    @Nullable
    public String getPlaceId() {
        return placeId;
    }

    @Nullable
    public Boolean isServesLunch() {
        return servesLunch;
    }

    @Nullable
    public Boolean isDelivery() {
        return delivery;
    }

    @Nullable
    public List<String> getTypes() {
        return types;
    }

    @Nullable
    public String getWebsite() {
        return website;
    }

    @Nullable
    public String getBusinessStatus() {
        return businessStatus;
    }

    @Nullable
    public List<AddressComponentsItem> getAddressComponents() {
        return addressComponents;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public Integer getPriceLevel() {
        return priceLevel;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    @Nullable
    public Geometry getGeometry() {
        return geometry;
    }

    @Nullable
    public String getVicinity() {
        return vicinity;
    }

    @Nullable
    public PlusCode getPlusCode() {
        return plusCode;
    }

    @Nullable
    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    @Nullable
    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    @Nullable
    public Boolean isServesBeer() {
        return servesBeer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsResponse that = (RestaurantDetailsResponse) o;
        return Objects.equals(utcOffset, that.utcOffset) && Objects.equals(formattedAddress, that.formattedAddress) && Objects.equals(wheelchairAccessibleEntrance, that.wheelchairAccessibleEntrance) && Objects.equals(reservable, that.reservable) && Objects.equals(icon, that.icon) && Objects.equals(rating, that.rating) && Objects.equals(iconBackgroundColor, that.iconBackgroundColor) && Objects.equals(takeout, that.takeout) && Objects.equals(photos, that.photos) && Objects.equals(editorialSummary, that.editorialSummary) && Objects.equals(reference, that.reference) && Objects.equals(dineIn, that.dineIn) && Objects.equals(currentOpeningHours, that.currentOpeningHours) && Objects.equals(userRatingsTotal, that.userRatingsTotal) && Objects.equals(reviews, that.reviews) && Objects.equals(servesDinner, that.servesDinner) && Objects.equals(iconMaskBaseUri, that.iconMaskBaseUri) && Objects.equals(servesBrunch, that.servesBrunch) && Objects.equals(servesBreakfast, that.servesBreakfast) && Objects.equals(servesWine, that.servesWine) && Objects.equals(adrAddress, that.adrAddress) && Objects.equals(placeId, that.placeId) && Objects.equals(servesLunch, that.servesLunch) && Objects.equals(delivery, that.delivery) && Objects.equals(types, that.types) && Objects.equals(website, that.website) && Objects.equals(businessStatus, that.businessStatus) && Objects.equals(addressComponents, that.addressComponents) && Objects.equals(url, that.url) && Objects.equals(priceLevel, that.priceLevel) && Objects.equals(name, that.name) && Objects.equals(openingHours, that.openingHours) && Objects.equals(geometry, that.geometry) && Objects.equals(vicinity, that.vicinity) && Objects.equals(plusCode, that.plusCode) && Objects.equals(formattedPhoneNumber, that.formattedPhoneNumber) && Objects.equals(internationalPhoneNumber, that.internationalPhoneNumber) && Objects.equals(servesBeer, that.servesBeer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utcOffset, formattedAddress, wheelchairAccessibleEntrance, reservable, icon, rating, iconBackgroundColor, takeout, photos, editorialSummary, reference, dineIn, currentOpeningHours, userRatingsTotal, reviews, servesDinner, iconMaskBaseUri, servesBrunch, servesBreakfast, servesWine, adrAddress, placeId, servesLunch, delivery, types, website, businessStatus, addressComponents, url, priceLevel, name, openingHours, geometry, vicinity, plusCode, formattedPhoneNumber, internationalPhoneNumber, servesBeer);
    }

    @NonNull
    @Override
    public String toString() {
        return "RestaurantDetailsResponse{" +
            "utcOffset=" + utcOffset +
            ", formattedAddress='" + formattedAddress + '\'' +
            ", wheelchairAccessibleEntrance=" + wheelchairAccessibleEntrance +
            ", reservable=" + reservable +
            ", icon='" + icon + '\'' +
            ", rating=" + rating +
            ", iconBackgroundColor='" + iconBackgroundColor + '\'' +
            ", takeout=" + takeout +
            ", photos=" + photos +
            ", editorialSummary=" + editorialSummary +
            ", reference='" + reference + '\'' +
            ", dineIn=" + dineIn +
            ", currentOpeningHours=" + currentOpeningHours +
            ", userRatingsTotal=" + userRatingsTotal +
            ", reviews=" + reviews +
            ", servesDinner=" + servesDinner +
            ", iconMaskBaseUri='" + iconMaskBaseUri + '\'' +
            ", servesBrunch=" + servesBrunch +
            ", servesBreakfast=" + servesBreakfast +
            ", servesWine=" + servesWine +
            ", adrAddress='" + adrAddress + '\'' +
            ", placeId='" + placeId + '\'' +
            ", servesLunch=" + servesLunch +
            ", delivery=" + delivery +
            ", types=" + types +
            ", website='" + website + '\'' +
            ", businessStatus='" + businessStatus + '\'' +
            ", addressComponents=" + addressComponents +
            ", url='" + url + '\'' +
            ", priceLevel=" + priceLevel +
            ", name='" + name + '\'' +
            ", openingHours=" + openingHours +
            ", geometry=" + geometry +
            ", vicinity='" + vicinity + '\'' +
            ", plusCode=" + plusCode +
            ", formattedPhoneNumber='" + formattedPhoneNumber + '\'' +
            ", internationalPhoneNumber='" + internationalPhoneNumber + '\'' +
            ", servesBeer=" + servesBeer +
            '}';
    }
}
