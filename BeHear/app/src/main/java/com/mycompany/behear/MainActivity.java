package com.mycompany.behear;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity  implements OnMapReadyCallback{
        static private GoogleMap mMap;
        //connect to "currentpoint" the current location from the gps data
        static final LatLng currentpoint = new LatLng(35.208,31.781);
        static HashMap<Integer, StatArea> statAreaTable;
        static public Manager manager;
        MapHelper mapHelper;
        boolean activityFlag;
        CheckBox votesBox;
        CheckBox econBox;
        CheckBox dataBox;
        CheckBox offlineModeBox;
        static Marker offlineModeMarker = null;
        static boolean offlineModeFlag = false;
        static LatLng offlineMarkerLatLng = null;
        private ArrayList<Marker> currentIcons;
        private ArrayList<Marker> currentData;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                votesBox = (CheckBox)findViewById(R.id.checkbox_votes);
                dataBox = (CheckBox)findViewById(R.id.checkbox_data);
                econBox = (CheckBox)findViewById(R.id.checkbox_socio);
                offlineModeBox = (CheckBox)findViewById(R.id.offlineMode);
                activityFlag = true;
                manager = new Manager(getApplicationContext());
                currentIcons = new ArrayList<>();
                currentData = new ArrayList<>();
                if (mMap == null) {
                        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                }
                mapHelper = new MapHelper(getApplicationContext());
                mapHelper.setData(mMap, currentData, 15);
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
                                } else {
                                        votesBox.setChecked(false);
                                        Manager.votesBoxFlag = false;
                                }
                                OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                                offlineLifeCycle.execute();


                        }
                });

                econBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        econBox.setChecked(true);
                                        Manager.econBoxFlag = true;
                                } else {
                                        econBox.setChecked(false);
                                        Manager.econBoxFlag = false;
                                }
                                OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                                offlineLifeCycle.execute();

                        }
                });

                dataBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        dataBox.setChecked(true);
                                        Manager.eduBoxFlag = true;
                                        mapHelper.setMarkers(mMap, currentIcons, 15);
                                } else {
                                        dataBox.setChecked(false);
                                        Manager.eduBoxFlag = false;
                                        for(Marker marker: currentIcons) {
                                                marker.setVisible(false);
                                        }
                                }
                                OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                                offlineLifeCycle.execute();

                        }
                });

                offlineModeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        offlineModeBox.setChecked(true);
                                        offlineModeFlag = true;
                                } else {
                                        dataBox.setChecked(false);
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
                                                .draggable(true));
                                        offlineModeMarker.showInfoWindow();
                                        offlineMarkerLatLng = offlineModeMarker.getPosition();
                                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(Marker arg0) {
                                                        if (offlineModeMarker.equals(arg0)) {
                                                                OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                                                                offlineLifeCycle.execute();
                                                        }
                                                        return true;
                                                }

                                        });

                                        offlineMarkerLatLng = offlineModeMarker.getPosition();
                                        OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                                        offlineLifeCycle.execute();
                                }
                        }
                });

                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                                boolean visability = (cameraPosition.zoom > 14);
                                for(Marker marker: currentIcons) {
                                        marker.setVisible(visability);
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
                OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                offlineLifeCycle.execute();
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
                offlineMarkerLatLng = offlineModeMarker.getPosition();
        }
});
                Point pnt = manager.getCurrentCoordinate();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pnt.getLat(), pnt.getLong()), 15));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                }
//                Daemon d = new Daemon();
//                d.execute();

        }

        @Override
        protected void onResume() {
                super.onResume();
                if (MainActivity.statAreaTable == null)
                        Manager.BeHearInit(getApplicationContext());
                setUpMapIfNeeded();
        }

        private void setUpMapIfNeeded() {
                if (mMap != null) {
                        return;
                }
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                //To do initialize map properly here
                if (mMap == null) {
                        return;
                }
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
                                        new AlertDialog.Builder(getApplicationContext())
                                                .setTitle("Location information")
                                                .setMessage("For this Application please confirm location permissions.\n You may revoke the permission later.")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                        }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();

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
                                if (offlineModeFlag)
                                        return null;
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


        private class OfflineDaemon extends AsyncTask<Boolean, Point, String> {

                @Override
                protected String doInBackground(Boolean... params) {
                        Point currentLocation;
                        if (offlineModeFlag && offlineMarkerLatLng != null)
                                currentLocation = new Point(Double.valueOf(Location.convert(offlineMarkerLatLng.longitude, Location.FORMAT_DEGREES)), Double.valueOf(Location.convert(offlineMarkerLatLng.latitude, Location.FORMAT_DEGREES)));
                        else
                                currentLocation = manager.getCurrentCoordinate();
                        if (currentLocation != null)
                                manager.startLifeCycle(currentLocation);
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

        static public void updateMap(Context context) {
                Point pnt = manager.getCurrentCoordinate();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pnt.getLat(), pnt.getLong()), 15));
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);

                }
        }

}

