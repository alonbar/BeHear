package com.mycompany.behear;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
<<<<<<< HEAD
        static HashMap<Integer, StatArea> statAreaTeable;
=======
        static HashMap<Integer, StatArea> statAreaTable;
>>>>>>> newBracnh
        MapManager mm;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                CheckBox satView = (CheckBox)findViewById(R.id.checkbox_votes);
                mm = new MapManager(getApplicationContext());
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mm.init();
                        Point pnt = mm.getCurrentCooredinate();
                        int a = 0 ;

                }
                else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                }

                satView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   Point CurrentLocation = mm.getCurrentCooredinate();
<<<<<<< HEAD
                                                                   String alert = "long: " + String.valueOf(CurrentLocation.x) + "lat: " + String.valueOf(CurrentLocation.y) ;
=======
                                                                   String alert = "long: " + String.valueOf(CurrentLocation.x) + " lat: " + String.valueOf(CurrentLocation.y) ;
>>>>>>> newBracnh
                                                                   Toast.makeText(getApplicationContext(), alert, Toast.LENGTH_LONG).show();
                                                           }
                                                   }
                );


//                Manager mng = new Manager(getApplicationContext());


//                AudioManager am = getApplicatgion().getSystemService(Context.AUDIO_SERVICE);
//                AudioManager am1 = getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);

//                try{
//                        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
//                        Log.d("bla",String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)));
//                        LocationListener locationListener = new LocationListener() {
//                                @Override
//                                public void onLocationChanged(Location location) {
//                                        //these coordinates needs to be sent to the map
//                                        //and to the polygon function
//                                        Double lat = location.getLatitude();
//                                        Double lng = location.getLongitude();
//
//                                }
//
//                                @Override
//                                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                                }
//
//                                @Override
//                                public void onProviderEnabled(String provider) {
//
//                                }
//
//                                @Override
//                                public void onProviderDisabled(String provider) {
//
//                                }
//                        };
//
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//                }catch(SecurityException e){
//                        Log.d("bla", e.getMessage());
//                }




                // Move the camera instantly to hamburg with a zoom of 15.
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentpoint, 20));
                //CameraUpdateFactory.newLatLngZoom(currentpoint, 10);
                // Zoom in, animating the camera.
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }



//        @Override
//        protected void onResume() {
//                super.onResume();
//                setUpMapIfNeeded();
//        }
//
//        private void setUpMapIfNeeded() {
//                if (mMap != null) {
//                        return;
//                }
//                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//                if (mMap == null) {
//                        return;
//                }
//                // Initialize map options. For example:
//                // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.781, 35.211), 10));
//        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
                switch (requestCode) {
                        case 1: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        mm.init();
                                        // permission was granted, yay! Do the
                                        // contacts-related task you need to do.

                                } else {

                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                }
                                return;
                        }

                        // other 'case' lines to check for other
                        // permissions this app might request
                }
        }

}
