package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class LocationRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FusedLocationProviderClient fusedLocationProviderClient = Mockito.mock(FusedLocationProviderClient.class);

    private LocationRepository locationRepository;

    private ArgumentCaptor<LocationCallback> callback = ArgumentCaptor.forClass(LocationCallback.class);

    @Before
    public void setUp() {
        locationRepository = Mockito.spy(new LocationRepository(fusedLocationProviderClient, Mockito.mock(Looper.class)));
    }

    @Test
    public void verify_startLocationRequest() {
        // When
        locationRepository.startLocationRequest();

        // Then
        Mockito.verify(fusedLocationProviderClient).removeLocationUpdates(callback.capture());
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), callback.capture(), any());
        Mockito.verifyNoMoreInteractions(fusedLocationProviderClient);
    }

    @Test
    public void verify_if_callBack_is_null_new_LocationCallback_is_called() {
        // Given
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback firstLocation = callback.getValue();

        // When
        locationRepository.stopLocationRequest();
        Mockito.verify(fusedLocationProviderClient, Mockito.times(2)).removeLocationUpdates(callback.capture());
        LocationCallback removedLocation = callback.getValue();

        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient, Mockito.times(2)).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback secondLocation = callback.getValue();

        // Then
        assertEquals(firstLocation, removedLocation);
        assertNotEquals(firstLocation, secondLocation);
    }

    @Test
    public void verify_if_callBack_is_notNull_same_callback_is_used() {
        // Given
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback firstLocation = callback.getValue();

        // When
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient, Mockito.times(2)).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback secondLocation = callback.getValue();

        // Then
        assertEquals(firstLocation, secondLocation);
    }


    @Test
    public void verify_stopLocationRequest() {
        // Given
        locationRepository.startLocationRequest();

        // When
        locationRepository.stopLocationRequest();

        // Then
        Mockito.verify(fusedLocationProviderClient, Mockito.times(2)).removeLocationUpdates(any(LocationCallback.class));
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), any(LocationCallback.class), any());
        Mockito.verifyNoMoreInteractions(fusedLocationProviderClient);
    }

    @Test
    public void verify_stopLocationRequest_without_start_should_not_do_anything() {
        // When
        locationRepository.stopLocationRequest();

        // Then
        Mockito.verifyNoMoreInteractions(fusedLocationProviderClient);
    }

    @Test
    public void verify_stopLocationRequest_twice() {
        // Given
        locationRepository.startLocationRequest();
        locationRepository.stopLocationRequest();

        // When
        locationRepository.stopLocationRequest();

        // Then
        Mockito.verify(fusedLocationProviderClient, Mockito.times(2)).removeLocationUpdates(any(LocationCallback.class));
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), any(LocationCallback.class), any());
        Mockito.verifyNoMoreInteractions(fusedLocationProviderClient);
    }
}


