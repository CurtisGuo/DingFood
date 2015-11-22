package com.ding.dingfood.backend;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by gene on 11/15/15.
 */
public class geolocation {

    double longtitude, latitude;
    private LocationManager locationManager;

    public void get_geolocation(LocationManager mlocationManager){
        locationManager = mlocationManager;
    }

    protected void showCurrentLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longtitude=location.getLongitude();
            latitude=location.getLatitude();
        }
    }

}
