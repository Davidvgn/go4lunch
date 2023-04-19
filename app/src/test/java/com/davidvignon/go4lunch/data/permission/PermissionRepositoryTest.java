package com.davidvignon.go4lunch.data.permission;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;


public class PermissionRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    Context context = Mockito.mock(Context.class);


    PermissionRepository permissionRepository;

    @Before
    public void setUp() {


        when(context.getApplicationInfo()).thenReturn(new ApplicationInfo());
        when(context.getPackageManager()).thenReturn(Mockito.mock(PackageManager.class));
        when(context.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt()))
            .thenReturn(PackageManager.PERMISSION_GRANTED);
        when(context.checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt()))
            .thenReturn(PackageManager.PERMISSION_GRANTED);
        when(context.checkPermission(eq(Manifest.permission.POST_NOTIFICATIONS), anyInt(), anyInt()))
            .thenReturn(PackageManager.PERMISSION_GRANTED);
        ApplicationInfo appInfo = new ApplicationInfo();
        appInfo.targetSdkVersion = Build.VERSION_CODES.M;
        when(context.getApplicationInfo()).thenReturn(appInfo);

        permissionRepository = new PermissionRepository(context);
    }

    @Test
    public void outside_conditions_location_permission_are_granted() {

        Boolean permissionGranted = LiveDataTestUtils.getValueForTesting(permissionRepository.isUserLocationGrantedLiveData());

        assertTrue(permissionGranted);

    }

    @Test
    public void outside_conditions_isNotificationPermissionGranted_returns_true() {
        assertTrue(permissionRepository.isNotificationPermissionGranted());
    }
}
