package com.mycompany.behear;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends FragmentActivity {
        private GoogleMap mMap;
        //connect to "currentpoint" the current location from the gps data
        static final LatLng currentpoint = new LatLng(35.208,31.781);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                setUpMapIfNeeded();

                try{
                        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                        Log.d("bla",String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)));
                        LocationListener locationListener = new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                        //these coordinates needs to be sent to the map
                                        //and to the polygon function
                                        Double lat = location.getLatitude();
                                        Double lng = location.getLongitude();

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

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                }catch(SecurityException e){
                        Log.d("bla", e.getMessage());
                }


                // Move the camera instantly to hamburg with a zoom of 15.
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentpoint, 20));
                //CameraUpdateFactory.newLatLngZoom(currentpoint, 10);
                // Zoom in, animating the camera.
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }



        @Override
        protected void onResume() {
                super.onResume();
                setUpMapIfNeeded();
        }

        private void setUpMapIfNeeded() {
                if (mMap != null) {
                        return;
                }
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                if (mMap == null) {
                        return;
                }
                // Initialize map options. For example:
                // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.781, 35.211), 10));
        }

}
