package com.davidvignon.go4lunch.data;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import retrofit2.Call;
import retrofit2.Response;

public class MapRepository {

    private final PlacesApi placesApi;
    public GoogleMap googleMap;


    public MapRepository(PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public PlacesApi getPlacesApi() {

        RetrofitService.getPlacesApi();

        placesApi.getNearbySearchResponse("45.7282,4.8307",
                    "1500",
                    "restaurant",
                    "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8").enqueue(new retrofit2.Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                    NearbySearchResponse nearbySearchResponse = response.body();
//
                    for (ResultsItem result : nearbySearchResponse.getResults()) {
                        googleMap.addMarker(
                            new MarkerOptions()
                                .position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()))
                                .title(result.getName())
                                .alpha(0.8f)
                                .icon(BitmapDescriptorFactory.defaultMarker(HUE_ROSE))
                        );
                    }
                }

                @Override
                public void onFailure(Call<NearbySearchResponse> call, Throwable t) {

                }
            });

        return placesApi;
    }


}
