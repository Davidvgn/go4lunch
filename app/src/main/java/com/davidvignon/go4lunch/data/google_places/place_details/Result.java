package com.davidvignon.go4lunch.data.google_places.place_details;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Result{

    @SerializedName("utc_offset")
    private int utcOffset;

    @SerializedName("formatted_address")
    private String formattedAddress;

    @SerializedName("wheelchair_accessible_entrance")
    private boolean wheelchairAccessibleEntrance;

    @SerializedName("reservable")
    private boolean reservable;

    @SerializedName("icon")
    private String icon;

    @SerializedName("rating")
    private double rating;

    @SerializedName("icon_background_color")
    private String iconBackgroundColor;

    @SerializedName("takeout")
    private boolean takeout;

    @SerializedName("photos")
    private List<PhotosItem> photos;

    @SerializedName("editorial_summary")
    private EditorialSummary editorialSummary;

    @SerializedName("reference")
    private String reference;

    @SerializedName("dine_in")
    private boolean dineIn;

    @SerializedName("current_opening_hours")
    private CurrentOpeningHours currentOpeningHours;

    @SerializedName("user_ratings_total")
    private int userRatingsTotal;

    @SerializedName("reviews")
    private List<ReviewsItem> reviews;

    @SerializedName("serves_dinner")
    private boolean servesDinner;

    @SerializedName("icon_mask_base_uri")
    private String iconMaskBaseUri;

    @SerializedName("serves_brunch")
    private boolean servesBrunch;

    @SerializedName("serves_breakfast")
    private boolean servesBreakfast;

    @SerializedName("serves_wine")
    private boolean servesWine;

    @SerializedName("adr_address")
    private String adrAddress;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("serves_lunch")
    private boolean servesLunch;

    @SerializedName("delivery")
    private boolean delivery;

    @SerializedName("types")
    private List<String> types;

    @SerializedName("website")
    private String website;

    @SerializedName("business_status")
    private String businessStatus;

    @SerializedName("address_components")
    private List<AddressComponentsItem> addressComponents;

    @SerializedName("url")
    private String url;

    @SerializedName("price_level")
    private int priceLevel;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("plus_code")
    private PlusCode plusCode;

    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @SerializedName("international_phone_number")
    private String internationalPhoneNumber;

    @SerializedName("serves_beer")
    private boolean servesBeer;

    public int getUtcOffset(){
        return utcOffset;
    }

    public String getFormattedAddress(){
        return formattedAddress;
    }

    public boolean isWheelchairAccessibleEntrance(){
        return wheelchairAccessibleEntrance;
    }

    public boolean isReservable(){
        return reservable;
    }

    public String getIcon(){
        return icon;
    }

    public double getRating(){
        return rating;
    }

    public String getIconBackgroundColor(){
        return iconBackgroundColor;
    }

    public boolean isTakeout(){
        return takeout;
    }

    public List<PhotosItem> getPhotos(){
        return photos;
    }

    public EditorialSummary getEditorialSummary(){
        return editorialSummary;
    }

    public String getReference(){
        return reference;
    }

    public boolean isDineIn(){
        return dineIn;
    }

    public CurrentOpeningHours getCurrentOpeningHours(){
        return currentOpeningHours;
    }

    public int getUserRatingsTotal(){
        return userRatingsTotal;
    }

    public List<ReviewsItem> getReviews(){
        return reviews;
    }

    public boolean isServesDinner(){
        return servesDinner;
    }

    public String getIconMaskBaseUri(){
        return iconMaskBaseUri;
    }

    public boolean isServesBrunch(){
        return servesBrunch;
    }

    public boolean isServesBreakfast(){
        return servesBreakfast;
    }

    public boolean isServesWine(){
        return servesWine;
    }

    public String getAdrAddress(){
        return adrAddress;
    }

    public String getPlaceId(){
        return placeId;
    }

    public boolean isServesLunch(){
        return servesLunch;
    }

    public boolean isDelivery(){
        return delivery;
    }

    public List<String> getTypes(){
        return types;
    }

    public String getWebsite(){
        return website;
    }

    public String getBusinessStatus(){
        return businessStatus;
    }

    public List<AddressComponentsItem> getAddressComponents(){
        return addressComponents;
    }

    public String getUrl(){
        return url;
    }

    public int getPriceLevel(){
        return priceLevel;
    }

    public String getName(){
        return name;
    }

    public OpeningHours getOpeningHours(){
        return openingHours;
    }

    public Geometry getGeometry(){
        return geometry;
    }

    public String getVicinity(){
        return vicinity;
    }

    public PlusCode getPlusCode(){
        return plusCode;
    }

    public String getFormattedPhoneNumber(){
        return formattedPhoneNumber;
    }

    public String getInternationalPhoneNumber(){
        return internationalPhoneNumber;
    }

    public boolean isServesBeer(){
        return servesBeer;
    }
}