package com.davidvignon.go4lunch.data.google_places.autocomplete;

import androidx.annotation.Keep;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@Keep
public class PredictionsItem{

    @SerializedName("reference")
    private final String reference;

    @SerializedName("types")
    private final List<String> types;

    @SerializedName("matched_substrings")
    private final List<MatchedSubstringsItem> matchedSubstrings;

    @SerializedName("terms")
    private final List<TermsItem> terms;

    @SerializedName("structured_formatting")
    private final StructuredFormatting structuredFormatting;

    @SerializedName("description")
    private final String description;

    public PredictionsItem(String reference, List<String> types, List<MatchedSubstringsItem> matchedSubstrings, List<TermsItem> terms, StructuredFormatting structuredFormatting, String description, String placeId) {
        this.reference = reference;
        this.types = types;
        this.matchedSubstrings = matchedSubstrings;
        this.terms = terms;
        this.structuredFormatting = structuredFormatting;
        this.description = description;
        this.placeId = placeId;
    }

    @SerializedName("place_id")
    private final String placeId;

    public String getReference(){
        return reference;
    }

    public List<String> getTypes(){
        return types;
    }

    public List<MatchedSubstringsItem> getMatchedSubstrings(){
        return matchedSubstrings;
    }

    public List<TermsItem> getTerms(){
        return terms;
    }

    public StructuredFormatting getStructuredFormatting(){
        return structuredFormatting;
    }

    public String getDescription(){
        return description;
    }

    public String getPlaceId(){
        return placeId;
    }
}