package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
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

    private static final double DEFAULT_LATITUDE = 45.757830302;
    private static final double DEFAULT_LONGITUDE = 4.823496706;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

   private final Location location = Mockito.mock(Location.class);


    @Before
    public void setUp() {
        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Mockito.doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        locationMutableLiveData.setValue(location);

    }

    @Test
    public void nominal_case_getLocationLiveData(){
        // When
        Location locationTest = LiveDataTestUtils.getValueForTesting(locationRepository.getLocationLiveData());

        // Then
        assertEquals(locationTest, location);
    }
}


