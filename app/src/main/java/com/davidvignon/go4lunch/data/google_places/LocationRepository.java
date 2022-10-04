package com.davidvignon.go4lunch.data.google_places;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.MainApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class LocationRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @NonNull
    private final Context context;

    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private LocationCallback callback;

    @Inject
    public LocationRepository(
        @NonNull FusedLocationProviderClient fusedLocationProviderClient,
        @NonNull @ApplicationContext Context context
    ) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.context = context;
    }

    // TODO NINO PermissionRepository maybe ?
    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public LiveData<Location> getLocationLiveData() {
        if (hasLocationPermission()) {
            if (callback == null) {
                callback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();

                        locationMutableLiveData.setValue(location);
                    }
                };
            }

            fusedLocationProviderClient.removeLocationUpdates(callback);


            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                    .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                callback,
                Looper.getMainLooper()
            );

        }
        return locationMutableLiveData;
    }

    // TODO Utiliser la technique du onResume dans le MainViewModel pour peupler un PermissionRepository
    public void getPermission() {

//        locationPermissionRequest.launch(new String[]{
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        });
    }
}