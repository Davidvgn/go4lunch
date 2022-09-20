package com.davidvignon.go4lunch.ui.map;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.davidvignon.go4lunch.data.MapRepository;
import com.davidvignon.go4lunch.data.NearbySearchResponse;
import com.davidvignon.go4lunch.data.PlacesApi;
import com.davidvignon.go4lunch.data.ResultsItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragment extends SupportMapFragment {

    LatLng latLng = (new LatLng(45.7282, 4.8307));

    private static final Gson gson = new GsonBuilder().setLenient().create();
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final String BASE_URL = "https://maps.googleapis.com/";
    private static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                Marker marker = googleMap.addMarker(
                    new MarkerOptions()
                        .position(new LatLng(45.7282, 4.8307))
                        .title("Ninka Lyon Gerland")
                        .alpha(0.8f)
                );
                if (marker != null) {
                    marker.showInfoWindow();
                }


                PlacesApi placesApi = retrofit.create(PlacesApi.class);
//
                placesApi.getNearbySearchResponse("45.7282,4.8307",
                    "1500",
                    "restaurant",
                    "AIzaSyDkT_c3oskPdGbt3FhUgX_ykrpv5eXOBa8").enqueue(new Callback<NearbySearchResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                        NearbySearchResponse nearbySearchResponse = response.body();
//
                        for (ResultsItem result : nearbySearchResponse.getResults()) {
                            Marker responseMarker = googleMap.addMarker(
                                new MarkerOptions()
                                    .position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()))
                                    .title(result.getName())
                                    .alpha(0.8f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_ROSE))
                            );
                            if (responseMarker != null) {
                                responseMarker.showInfoWindow();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {

                    }
                });
            }
        });
    }
}
