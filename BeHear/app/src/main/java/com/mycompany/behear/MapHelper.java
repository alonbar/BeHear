package com.mycompany.behear;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.GoogleMap;

import java.util.jar.Manifest;

public class MapHelper {

    private Context context;
    private LocationManager locationManager;
    LocationListener locationListener;

    Point currentLocation;
    public MapHelper(Context context) {
        this.context = context;

    }

    public void init() {
        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            this.locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
            this.currentLocation = new Point(0,0);
            this.locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    currentLocation.setLong(lng);
                    currentLocation.setLat(lat);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
        }
    }
    //x = longtitue, y = lateitud
    public Point getCurrentCooredinate() {

        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (current == null){

//                return new Point (35.211183, 31.771384);
                return new Point (35.20999, 31.770876);
            }
            else {
                Point pnt = new Point(current.getLongitude(), current.getLatitude());
                return pnt;
            }

        }
        return new Point (-74.005941, 40.712784);
    }

    public void showCurrentCoordination() {

    }
}
