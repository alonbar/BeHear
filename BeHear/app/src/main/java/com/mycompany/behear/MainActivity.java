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


                try {

                        InputStream is = getApplicationContext().getAssets().open("json2.json");

                        int size = is.available();

                        byte[] buffer = new byte[size];

                        is.read(buffer);

                        is.close();

                        json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                        ex.printStackTrace();
                        return;
                }
                try {
                        JSONObject obj = new JSONObject(json);
                        HashMap<Integer, StatArea> statAreaTable = new HashMap<>();

                        for(int i = 0; i < obj.getJSONArray("features").length(); i++){
                            ArrayList<Point> geometry = new ArrayList<>();
                                for (int j = 0; j < obj.getJSONArray("features").getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).length(); j++) {
                                        geometry.add(new Point(obj.getJSONArray("features").getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).getJSONArray(j).getDouble(0),
                                                obj.getJSONArray("features").getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).getJSONArray(j).getDouble(1)));
                                }
                                StatArea objToInsert = new StatArea();
                                objToInsert.setPolygon(new Polygon(geometry));
                                    statAreaTable.put(new Integer(obj.getJSONArray("features").getJSONObject(i).getJSONObject("attributes").getInt("STAT08")), objToInsert);
                        }

                        //read kalpi data
                        InputStream in;
                        BufferedReader reader;
                        String line;
                        in = this.getAssets().open("Kalpi.csv");
                        reader = new BufferedReader(new InputStreamReader(in));
                        ArrayList<String> newKapli = new ArrayList<>();

                        while ((line = reader.readLine()) != null) {
                                Point pnt = new Point(Double.parseDouble(line.split(",")[line.split(",").length -1]), Double.parseDouble(line.split(",")[line.split(",").length -2]));
                                int polyID = -1;
                                for(Polygon currentPoly: polygonTable.values()) {
                                        if(currentPoly.isPointInPolygon(pnt)){
                                                polyID = currentPoly.getId();
                                                break;
                                        }
                                }
                                if (polyID == -1) {
                                        line += ",NULL";  }
                                else {
                                        line += "," + Integer.toString(polyID);
                                }
                                newKapli.add(line);
                        }
                        System.out.println(line);

                }
                catch (Exception e){
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
