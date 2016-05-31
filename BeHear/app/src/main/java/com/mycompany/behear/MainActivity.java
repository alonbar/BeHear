package com.mycompany.behear;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
        CheckBox votesBox;
        CheckBox econBox;
        CheckBox eduBox;
        CheckBox offlineModeBox;
        static Marker offlineModeMarker = null;
        static boolean offlineModeFlag = false;
        static LatLng offlineMarkerLatLng = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                votesBox = (CheckBox)findViewById(R.id.checkbox_votes);
                eduBox = (CheckBox)findViewById(R.id.checkbox_edu);
                econBox = (CheckBox)findViewById(R.id.checkbox_socio);
                offlineModeBox = (CheckBox)findViewById(R.id.offlineMode);
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


                votesBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        votesBox.setChecked(true);
                                        Manager.votesBoxFlag = true;
                                }
                                else {
                                        votesBox.setChecked(false);
                                        Manager.votesBoxFlag = false;
                                }
                        }
                });

                econBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        econBox.setChecked(true);
                                        Manager.econBoxFlag = true;
                                }
                                else {
                                        econBox.setChecked(false);
                                        Manager.econBoxFlag = false;
                                }
                        }
                });

                eduBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        eduBox.setChecked(true);
                                        Manager.eduBoxFlag = true;
                                }
                                else {
                                        eduBox.setChecked(false);
                                        Manager.econBoxFlag = true;
                                }

                        }
                });

                offlineModeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        offlineModeBox.setChecked(true);
                                        offlineModeFlag = true;
                                }
                                else {
                                        eduBox.setChecked(false);
                                        offlineModeFlag = false;
                                }

                        }
                });

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                        @Override
                        public void onMapClick(LatLng latLng) {
                                if (offlineModeBox.isChecked()) {
                                        if (offlineModeMarker != null) {
                                                offlineModeMarker.remove();
                                        }
                                        offlineModeMarker = mMap.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .draggable(true)
                                                .title("Now playing"));

                                        offlineMarkerLatLng = offlineModeMarker.getPosition();
                                }
                        }
                });

mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {


                                     @Override
                                     public void onMarkerDragStart(Marker marker) {

                                     }

                                     @Override
                                     public void onMarkerDrag(Marker marker) {
                                             offlineMarkerLatLng = offlineModeMarker.getPosition();
                                     }

                                     @Override
                                     public void onMarkerDragEnd(Marker marker) {
                                             offlineMarkerLatLng = offlineModeMarker.getPosition();
                                     }
                             });
                Daemon d = new Daemon();
                d.execute();

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

        private class Daemon extends AsyncTask<Boolean, Point, String> {

                @Override
                protected String doInBackground(Boolean... params) {
                        while(activityFlag == true){
                                Point pnt = null;
                                if (!offlineModeFlag) {
                                        pnt = manager.getCurrentCoordinate();
                                        publishProgress(pnt);

                                } else {
                                        if (offlineModeMarker != null) {
                                                pnt = new Point(Double.valueOf(Location.convert(offlineMarkerLatLng.longitude, Location.FORMAT_DEGREES)), Double.valueOf(Location.convert(offlineMarkerLatLng.latitude, Location.FORMAT_DEGREES)));
                                        }
                                }
                                if (pnt != null)
                                        manager.startLifeCycle(pnt);
                                try {
                                        Thread.sleep(1000);
                                } catch (Exception e) {
                                        return e.getMessage();
                                }
                        }
                        return "Executed";
                }

                @Override
                protected void onPostExecute(String result) {

                }

                @Override
                protected void onPreExecute() {}

                @Override
                protected void onProgressUpdate(Point... values) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(values[0].getLat(), values[0].getLong()), 15));
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mMap.setMyLocationEnabled(true);
                        }
                }

        }
}

