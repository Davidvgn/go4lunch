package com.davidvignon.go4lunch.data.google_places.place_details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RestaurantDetailsResponse {

    @Nullable
    @SerializedName("utc_offset")
    private int utcOffset;

    @Nullable
    @SerializedName("formatted_address")
    private String formattedAddress;

    @Nullable
    @SerializedName("wheelchair_accessible_entrance")
    private boolean wheelchairAccessibleEntrance;

    @Nullable
    @SerializedName("reservable")
    private boolean reservable;

    @Nullable
    @SerializedName("icon")
    private String icon;

    @Nullable
    @SerializedName("rating")
    private double rating;

    @Nullable
    @SerializedName("icon_background_color")
    private String iconBackgroundColor;

    @Nullable
    @SerializedName("takeout")
    private boolean takeout;

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
    private boolean dineIn;

    @Nullable
    @SerializedName("current_opening_hours")
    private CurrentOpeningHours currentOpeningHours;

    @Nullable
    @SerializedName("user_ratings_total")
    private int userRatingsTotal;

    @Nullable
    @SerializedName("reviews")
    private List<ReviewsItem> reviews;

    @Nullable
    @SerializedName("serves_dinner")
    private boolean servesDinner;

    @Nullable
    @SerializedName("icon_mask_base_uri")
    private String iconMaskBaseUri;

    @Nullable
    @SerializedName("serves_brunch")
    private boolean servesBrunch;

    @Nullable
    @SerializedName("serves_breakfast")
    private boolean servesBreakfast;

    @Nullable
    @SerializedName("serves_wine")
    private boolean servesWine;

    @Nullable
    @SerializedName("adr_address")
    private String adrAddress;

    @Nullable
    @SerializedName("place_id")
    private String placeId;

    @Nullable
    @SerializedName("serves_lunch")
    private boolean servesLunch;

    @Nullable
    @SerializedName("delivery")
    private boolean delivery;

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
    private int priceLevel;

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
    private boolean servesBeer;

    public RestaurantDetailsResponse(
       @Nullable int utcOffset,
       @Nullable String formattedAddress,
       @Nullable boolean wheelchairAccessibleEntrance,
       @Nullable boolean reservable,
       @Nullable String icon,
       @Nullable double rating,
       @Nullable String iconBackgroundColor,
       @Nullable boolean takeout,
       @Nullable List<PhotosItem> photos,
       @Nullable EditorialSummary editorialSummary,
       @Nullable String reference,
       @Nullable boolean dineIn,
       @Nullable CurrentOpeningHours currentOpeningHours,
       @Nullable int userRatingsTotal,
       @Nullable List<ReviewsItem> reviews,
       @Nullable boolean servesDinner,
       @Nullable String iconMaskBaseUri,
       @Nullable boolean servesBrunch,
       @Nullable boolean servesBreakfast,
       @Nullable boolean servesWine,
       @Nullable String adrAddress,
       @Nullable String placeId,
       @Nullable boolean servesLunch,
       @Nullable boolean delivery,
       @Nullable List<String> types,
       @Nullable String website,
       @Nullable String businessStatus,
       @Nullable List<AddressComponentsItem> addressComponents,
       @Nullable String url,
       @Nullable int priceLevel,
       @Nullable String name,
       @Nullable OpeningHours openingHours,
       @Nullable Geometry geometry,
       @Nullable String vicinity,
       @Nullable PlusCode plusCode,
       @Nullable String formattedPhoneNumber,
       @Nullable String internationalPhoneNumber,
       @Nullable boolean servesBeer
    ) {
        this.utcOffset = utcOffset;
        this.formattedAddress = formattedAddress;
        this.wheelchairAccessibleEntrance = wheelchairAccessibleEntrance;
        this.reservable = reservable;
        this.icon = icon;
        this.rating = rating;
        this.iconBackgroundColor = iconBackgroundColor;
        this.takeout = takeout;
        this.photos = photos;
        this.editorialSummary = editorialSummary;
        this.reference = reference;
        this.dineIn = dineIn;
        this.currentOpeningHours = currentOpeningHours;
        this.userRatingsTotal = userRatingsTotal;
        this.reviews = reviews;
        this.servesDinner = servesDinner;
        this.iconMaskBaseUri = iconMaskBaseUri;
        this.servesBrunch = servesBrunch;
        this.servesBreakfast = servesBreakfast;
        this.servesWine = servesWine;
        this.adrAddress = adrAddress;
        this.placeId = placeId;
        this.servesLunch = servesLunch;
        this.delivery = delivery;
        this.types = types;
        this.website = website;
        this.businessStatus = businessStatus;
        this.addressComponents = addressComponents;
        this.url = url;
        this.priceLevel = priceLevel;
        this.name = name;
        this.openingHours = openingHours;
        this.geometry = geometry;
        this.vicinity = vicinity;
        this.plusCode = plusCode;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.servesBeer = servesBeer;
    }

    @Nullable
    public int getUtcOffset(){
        return utcOffset;
    }

    @Nullable
    public String getFormattedAddress(){
        return formattedAddress;
    }

    @Nullable
    public boolean isWheelchairAccessibleEntrance(){
        return wheelchairAccessibleEntrance;
    }

    @Nullable
    public boolean isReservable(){
        return reservable;
    }

    @Nullable
    public String getIcon(){
        return icon;
    }

    @Nullable
    public double getRating(){
        return rating;
    }

    @Nullable
    public String getIconBackgroundColor(){
        return iconBackgroundColor;
    }

    @Nullable
    public boolean isTakeout(){
        return takeout;
    }

    @Nullable
    public List<PhotosItem> getPhotos(){
        return photos;
    }

    @Nullable
    public EditorialSummary getEditorialSummary(){
        return editorialSummary;
    }

    @Nullable
    public String getReference(){
        return reference;
    }

    @Nullable
    public boolean isDineIn(){
        return dineIn;
    }

    @Nullable
    public CurrentOpeningHours getCurrentOpeningHours(){
        return currentOpeningHours;
    }

    @Nullable
    public int getUserRatingsTotal(){
        return userRatingsTotal;
    }

    @Nullable
    public List<ReviewsItem> getReviews(){
        return reviews;
    }

    @Nullable
    public boolean isServesDinner(){
        return servesDinner;
    }

    @Nullable
    public String getIconMaskBaseUri(){
        return iconMaskBaseUri;
    }

    @Nullable
    public boolean isServesBrunch(){
        return servesBrunch;
    }

    @Nullable
    public boolean isServesBreakfast(){
        return servesBreakfast;
    }

    @Nullable
    public boolean isServesWine(){
        return servesWine;
    }

    @Nullable
    public String getAdrAddress(){
        return adrAddress;
    }

    @Nullable
    public String getPlaceId(){
        return placeId;
    }

    @Nullable
    public boolean isServesLunch(){
        return servesLunch;
    }

    @Nullable
    public boolean isDelivery(){
        return delivery;
    }

    @Nullable
    public List<String> getTypes(){
        return types;
    }

    @Nullable
    public String getWebsite(){
        return website;
    }

    @Nullable
    public String getBusinessStatus(){
        return businessStatus;
    }

    @Nullable
    public List<AddressComponentsItem> getAddressComponents(){
        return addressComponents;
    }

    @Nullable
    public String getUrl(){
        return url;
    }

    @Nullable
    public int getPriceLevel(){
        return priceLevel;
    }

    @Nullable
    public String getName(){
        return name;
    }

    @Nullable
    public OpeningHours getOpeningHours(){
        return openingHours;
    }

    @Nullable
    public Geometry getGeometry(){
        return geometry;
    }

    @Nullable
    public String getVicinity(){
        return vicinity;
    }

    @Nullable
    public PlusCode getPlusCode(){
        return plusCode;
    }

    @Nullable
    public String getFormattedPhoneNumber(){
        return formattedPhoneNumber;
    }

    @Nullable
    public String getInternationalPhoneNumber(){
        return internationalPhoneNumber;
    }

    @Nullable
    public boolean isServesBeer(){
        return servesBeer;
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
