package com.davidvignon.go4lunch.data.permission;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class PermissionRepository {

    @NonNull
    private final Context context;

    @Inject
    public PermissionRepository(@NonNull @ApplicationContext Context context) {
        this.context = context;
    }

    public boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }
}
