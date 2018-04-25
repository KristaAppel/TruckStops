package com.kristaappel.truckstops.objects;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

import static android.content.Context.LOCATION_SERVICE;

public class LocationHelper {

    @SuppressWarnings({"MissingPermission"}) // Permissions is being checked in locationPermissionCheck()
    public static void startLocationUpdates(Context context, LocationListener locationListener) {
        LocationManager locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000, 10.0f, locationListener);
    }


    @SuppressWarnings({"MissingPermission"})
    public static void stopLocationUpdates(Context context, LocationListener locationListener) {
        LocationManager locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }
}
