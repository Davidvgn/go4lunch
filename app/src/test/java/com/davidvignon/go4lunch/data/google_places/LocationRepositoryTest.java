package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

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
    public void verify_startLocationRequest(){
        // Given
        locationRepository.stopLocationRequest();

        // When
        locationRepository.startLocationRequest();

        // Then
        assertNotNull(callback);
        Mockito.verify(fusedLocationProviderClient).removeLocationUpdates(callback.capture());
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), any(LocationCallback.class), any());
    }

    @Test
    public void test() {
        LocationCallback loc = callback.getValue();

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


