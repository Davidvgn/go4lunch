package com.davidvignon.go4lunch.data.utils;

import android.location.Location;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DistanceCalculator {

    @Inject
    public DistanceCalculator() {
    }

    public int distanceBetween(double lat1, double long1, double lat2, double long2) {
        Location location1 = new Location("");
        location1.setLatitude(lat1);
        location1.setLongitude(long1);

        Location location2 = new Location("");
        location2.setLatitude(lat2);
        location2.setLongitude(long2);

        return (int) (location2).distanceTo(location2);
    }
}
