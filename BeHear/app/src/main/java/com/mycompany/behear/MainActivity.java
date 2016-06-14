package com.mycompany.behear;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.PolygonOptions;

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
        CheckBox sexualHarassmentBox;
        CheckBox propertyCrimeBox;
        CheckBox offlineModeBox;
        //ImageButton aboutBut;
        Button whatsBut;
        Button aboutBut;
        ImageButton aboutSocio;
        ImageButton aboutVotes;
        ImageButton aboutExplore;
        ImageButton aboutCrime;

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
                econBox = (CheckBox)findViewById(R.id.checkbox_socio);
                propertyCrimeBox = (CheckBox)findViewById(R.id.check_box_property_crime);
               // aboutBut = (ImageButton) findViewById(R.id.about);
                whatsBut = (Button) findViewById(R.id.whats);
                aboutBut = (Button) findViewById(R.id.about);
                aboutSocio = (ImageButton) findViewById(R.id.socio_qm);
                aboutVotes = (ImageButton) findViewById(R.id.votes_qm);
                aboutExplore = (ImageButton) findViewById(R.id.explore_qm);
                aboutCrime = (ImageButton) findViewById(R.id.crime_qm);
                offlineModeBox = (CheckBox) findViewById(R.id.offlineMode);
                activityFlag = true;
                manager = new Manager(getApplicationContext());
                currentIcons = new ArrayList<>();
                currentData = new ArrayList<>();
                if (mMap == null) {
                        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                }
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker marker) {
                                return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                              if (marker.getTitle() == null)
                                      return null;
                              View view = getLayoutInflater().inflate(R.layout.info_window, null);
                              TextView crimeText = (TextView)view.findViewById(R.id.crimeText);
                              TextView schoolText = (TextView)view.findViewById(R.id.schoolText);
                              TextView coinsText = (TextView)view.findViewById(R.id.coinsText);
                              String data = marker.getTitle();
                              String[] parsedData = data.split(" ");
                              coinsText.setText(parsedData[0]);
                              schoolText.setText(parsedData[1]);
                              crimeText.setText(parsedData[2]);
                              return view;

                        }
                });
                mapHelper = new MapHelper(getApplicationContext());
                mapHelper.setMarkers(mMap, currentIcons, 15);
                mapHelper.setData(mMap, currentData);

                if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                }

                mapHelper.init();

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
                                boolean visability = ((mMap.getCameraPosition().zoom > 14) && votesBox.isChecked());
                                for(Marker marker: currentIcons) {
                                        marker.setVisible(visability);
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

                propertyCrimeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                        propertyCrimeBox.setChecked(true);
                                        Manager.propertyBoxFlag= true;
                                } else {
                                        propertyCrimeBox.setChecked(false);
                                        Manager.propertyBoxFlag= false;
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
                                        offlineModeBox.setChecked(false);
                                        offlineModeFlag = false;
                                }
                                if (offlineModeMarker != null)
                                        offlineModeMarker.setVisible(isChecked);
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
                                        offlineMarkerLatLng = offlineModeMarker.getPosition();
                                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(Marker arg0) {

                                                        if (offlineModeMarker.equals(arg0)) {
                                                                OfflineDaemon offlineLifeCycle = new OfflineDaemon();
                                                                offlineLifeCycle.execute();
                                                        }
                                                        else {
                                                                if (arg0.getTitle() != null)
                                                                        arg0.showInfoWindow();
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
                                for (Marker marker : currentData) {
                                        marker.setVisible(visability);
                                }
                        }
                });

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                                marker.hideInfoWindow();
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

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        Point pnt = manager.getCurrentCoordinate();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pnt.getLat(), pnt.getLong()), 15));
                }

//                Daemon d = new Daemon();
//                d.execute();


                aboutBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                // Inflate and set the layout for the dialog
                                builder.setView(inflater.inflate(R.layout.about_dialog, null));
                                AlertDialog dialog = builder.create();
                                dialog.show();
                        }
                });

                whatsBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                // Inflate and set the layout for the dialog
                                builder.setView(inflater.inflate(R.layout.whats_dialog, null));
                                AlertDialog dialog = builder.create();
                                dialog.show();
                        }
                });
//
                aboutSocio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                // Inflate and set the layout for the dialog
                                builder.setView(inflater.inflate(R.layout.socio_dialog, null));
                                AlertDialog dialog = builder.create();
                                dialog.show();
                        }
                });

                aboutCrime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                // Inflate and set the layout for the dialog
                                builder.setView(inflater.inflate(R.layout.crime_dialog, null));
                                AlertDialog dialog = builder.create();
                                dialog.show();
                        }
                });

                aboutVotes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                // Inflate and set the layout for the dialog
                                builder.setView(inflater.inflate(R.layout.votes_dialog, null));
                                AlertDialog dialog = builder.create();
                                dialog.show();
                        }
                });

                aboutExplore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                // Inflate and set the layout for the dialog
                                builder.setView(inflater.inflate(R.layout.explore_dialog, null));
                                AlertDialog dialog = builder.create();
                                dialog.show();
                        }
                });
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
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                                mMap.setMyLocationEnabled(true);
                                        Point pnt = manager.getCurrentCoordinate();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pnt.getLat(), pnt.getLong()), 15));
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

