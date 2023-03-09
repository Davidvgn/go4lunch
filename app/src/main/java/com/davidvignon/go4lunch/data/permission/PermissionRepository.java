package com.davidvignon.go4lunch.data.permission;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public boolean isNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public LiveData<Boolean> isUserLocationGrantedLiveData() {
        MutableLiveData<Boolean> isGrantedMutableLiveData = new MutableLiveData<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isGrantedMutableLiveData.setValue(true);
            } else {
                isGrantedMutableLiveData.setValue(false);
            }
        } else {
            isGrantedMutableLiveData.setValue(true);
        }
        return isGrantedMutableLiveData;
    }

}
