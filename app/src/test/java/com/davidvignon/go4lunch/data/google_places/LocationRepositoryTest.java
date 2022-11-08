package com.davidvignon.go4lunch.data.google_places;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import android.location.Location;
import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
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

    @Before
    public void setUp() {
        locationRepository = Mockito.spy(new LocationRepository(fusedLocationProviderClient, Mockito.mock(Looper.class)));
    }

    @Test
    public void verify_startLocationRequest() {
        // When
        locationRepository.startLocationRequest();

        // Then
        Mockito.verify(fusedLocationProviderClient).removeLocationUpdates(any(LocationCallback.class));
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), any(LocationCallback.class), any());
        Mockito.verifyNoMoreInteractions(fusedLocationProviderClient);
    }

    @Test
    public void verify_if_callBack_is_notNull_same_callback_is_used() {
        // Given
        ArgumentCaptor<LocationCallback> callback = ArgumentCaptor.forClass(LocationCallback.class);
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback firstLocationCallback = callback.getValue();

        // When
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient, Mockito.times(2)).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback secondLocation = callback.getValue();

        // Then
        assertEquals(firstLocationCallback, secondLocation);
    }

    @Test
    public void given_startLocationRequest_when_onLocationResult_is_called_then_livedata_has_a_location() {
        // Given
        ArgumentCaptor<LocationCallback> callback = ArgumentCaptor.forClass(LocationCallback.class);
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback locationCallback = callback.getValue();

        LocationResult locationResult = Mockito.mock(LocationResult.class);
        Location expectedLocation = Mockito.mock(Location.class);
        Mockito.doReturn(expectedLocation).when(locationResult).getLastLocation();

        // When
        locationCallback.onLocationResult(locationResult);
        Location result = LiveDataTestUtils.getValueForTesting(locationRepository.getLocationLiveData());

        // Then
        assertEquals(expectedLocation, result);
    }

    @Test
    public void given_startLocationRequest_when_a_new_onLocationResult_is_called_then_livedata_has_a_new_location() {
        // Given
        ArgumentCaptor<LocationCallback> callback = ArgumentCaptor.forClass(LocationCallback.class);
        locationRepository.startLocationRequest();
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(any(), callback.capture(), any());
        LocationCallback locationCallback = callback.getValue();

        LocationResult locationResult = Mockito.mock(LocationResult.class);
        Location expectedLocation = Mockito.mock(Location.class);
        Mockito.doReturn(expectedLocation).when(locationResult).getLastLocation();
        locationCallback.onLocationResult(locationResult);

        LocationResult secondLocationResult = Mockito.mock(LocationResult.class);
        Location secondExpectedLocation = Mockito.mock(Location.class);
        Mockito.doReturn(secondExpectedLocation).when(secondLocationResult).getLastLocation();

        // When
        locationCallback.onLocationResult(secondLocationResult);
        Location result = LiveDataTestUtils.getValueForTesting(locationRepository.getLocationLiveData());

        // Then
        assertEquals(secondExpectedLocation, result);
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


