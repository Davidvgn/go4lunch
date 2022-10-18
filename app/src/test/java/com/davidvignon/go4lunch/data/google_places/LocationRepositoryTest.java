package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.internal.Mutable;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationRepositoryTest {

    private final FusedLocationProviderClient fusedLocationProviderClient = Mockito.mock(FusedLocationProviderClient.class);

    @Mock
    private Context context;

    @Mock
    private LocationRepository locationRepository;
    private  MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp(){
        locationRepository = new LocationRepository(fusedLocationProviderClient, context);
    }

    @Test
    public void test_getLocationLiveData(){
    }
}
