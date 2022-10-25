package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import org.junit.Before;
import org.junit.Rule;

import org.junit.Test;
import org.mockito.Mockito;

public class LocationRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FusedLocationProviderClient fusedLocationProviderClient = Mockito.mock(FusedLocationProviderClient.class);

    private final Context context = Mockito.mock(Context.class);

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();


    private LocationRepository locationRepository;

    @Before
    public void setUp() {
        locationRepository = new LocationRepository(fusedLocationProviderClient, context);

    }


    @Test
    public void test1(){

    }
}


