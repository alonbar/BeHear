package com.mycompany.behear;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MainActivity extends FragmentActivity  implements OnMapReadyCallback {
        private GoogleMap mMap;
        //connect to "currentpoint" the current location from the gps data
        static final LatLng currentpoint = new LatLng(35.208,31.781);
        static HashMap<Integer, StatArea> statAreaTable;
        Manager manager;
        MapHelper mapHelper;
        boolean activityFlag;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                Point point2 = new Point(35.210596,31.779684);
                for (StatArea curStat : MainActivity.statAreaTable.values()) {
                        if (curStat.getPolygon().isPointInPolygon(point2)) {
                                System.out.println("yes");
                        }
                }

                setContentView(R.layout.activity_main);
                CheckBox satView = (CheckBox)findViewById(R.id.checkbox_votes);
                activityFlag = true;
                manager = new Manager(getApplicationContext());
                if (mMap == null) {
                        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                }
                mapHelper = new MapHelper(getApplicationContext());
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mapHelper.init();

                }
                else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                }

                satView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   Point CurrentLocation = mapHelper.getCurrentCooredinate();
                                                                   String alert = "long: " + String.valueOf(CurrentLocation.getLong()) + " lat: " + String.valueOf(CurrentLocation.getLat()) ;
                                                                   Toast.makeText(getApplicationContext(), alert, Toast.LENGTH_LONG).show();
                                                           }
                                                   }
                );


                Daemon d = new Daemon();
                d.execute(null, null, null);

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

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
                switch (requestCode) {
                        case 1: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        mapHelper.init();
                                        // permission was granted, yay! Do the
                                        // contacts-related task you need to do.

                                } else {

                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                }
                                return;
                        }
                }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

        }

        private class Daemon extends AsyncTask<String, Point, String> {

                @Override
                protected String doInBackground(String... params) {
                        while(activityFlag = true){
                                Point pnt = manager.getCurrentCoordinate();
                                publishProgress(pnt);
                                manager.startLifeCycle();


                                try {
                                        Thread.sleep(1000);
                                }
                                catch (Exception e) {
                                        return e.getMessage();
                                }
                        }
                        return "Executed";
                }

                @Override
                protected void onPostExecute(String result) {
                        TextView txt = (TextView) findViewById(R.id.logo2);
                        txt.setText("Executed"); // txt.setText(result);
                        // might want to change "executed" for the returned string passed
                        // into onPostExecute() but that is upto you
                }

                @Override
                protected void onPreExecute() {}

                @Override
                protected void onProgressUpdate(Point... values) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(values[0].getLat(), values[0].getLong()), 15));
//                        mMap.addMarker(new MarkerOptions().position(new LatLng(values[0].getLat(),  values[0].getLong())).title("Marker"));
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mMap.setMyLocationEnabled(true);
                        }

                        TextView txt = (TextView) findViewById(R.id.logo2);
                        txt.setText("lat: " + String.valueOf(values[0].getLat()) + " lng: "+  String.valueOf(values[0].getLong()));
                        System.out.println(values[0]);

                }

        }
}

