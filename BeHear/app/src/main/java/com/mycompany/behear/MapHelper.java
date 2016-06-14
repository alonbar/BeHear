package com.mycompany.behear;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class MapHelper {

    private Context context;
    public static LocationManager locationManager;
    private float previousZoomLevel = -1.0f;
    private boolean isZooming = false;
    LocationListener locationListener;
    Point lastKnownLocation;
    boolean firstTime;
    static double maxDistance = 0.3;

    public MapHelper(Context context) {
        this.context = context;

    }

    public void init() {
        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            firstTime = true;
            this.locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
            this.locationListener = new LocationListener() {
                    @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    if (firstTime) {
                        firstTime = false;
                        lastKnownLocation = new Point(lng, lat);
                        return;
                    }
                    if (MainActivity.offlineModeFlag) {
                        lastKnownLocation = new Point(lng, lat);
                    }
                    else {
                       if ((lastKnownLocation != null) &&
                           (StatArea.distance(location.getLongitude(), location.getLatitude(), lastKnownLocation.getLong(), lastKnownLocation.getLat(), 'K') > maxDistance)) {
                            lastKnownLocation = new Point(lng, lat);
                           new Thread() {
                               public void run() {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           MainActivity.manager.startLifeCycle(lastKnownLocation);
                                       }
                                   });
                               }}.start();

                       }
                        MainActivity.updateMap(context);

                    }
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
            if (locationManager==null) {
                return new Point (-74.005941, 40.712784);
            }
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

    public static int getIcon(String party){
       int icon = -1;
        switch (party){
            case "T":
                icon = R.drawable.t_icon;
                break;
            case "G":
                icon = R.drawable.g_icon;
                break;
            case "A":
                icon = R.drawable.a_icon;
                break;
            case "K":
                icon = R.drawable.k_icon;
                break;
            case "L":
                icon = R.drawable.l_icon;
                break;
            case "M":
                icon = R.drawable.m_icon;
                break;
            case "S":
                icon = R.drawable.s_icon;
                break;
            case "V":
                icon = R.drawable.v_icon;
                break;
        }
        return icon;
    }

    public void setMarkers(GoogleMap mMap, ArrayList<Marker> currentIcons, float zoom) {
        if (currentIcons == null)
            currentIcons = new ArrayList<>();
            for (Kalpi kalpi: StatArea.kalpiList) {
                currentIcons.add(mMap.addMarker(new MarkerOptions().position(new LatLng(kalpi.getPoint().getLat(), kalpi.getPoint().getLong()))
                        .icon(BitmapDescriptorFactory.fromResource(getIcon(kalpi.getPopolarParty())))));
                currentIcons.get(currentIcons.size() -1 ).setVisible(false);
            }
    }

    public void setData(GoogleMap mMap, ArrayList<Marker> currentData){
        String[] curStatData;
        if(currentData == null)
            currentData = new ArrayList<>();
        for(StatArea stat : MainActivity.statAreaTable.values()){
            curStatData = stat.getData();
            currentData.add(mMap.addMarker(new MarkerOptions().position(new LatLng(stat.getPolygon().getCenter().getLat(), stat.getPolygon().getCenter().getLong()))
                    .title(curStatData[0] + " " + curStatData[1] + " " + curStatData[2]).icon(BitmapDescriptorFactory.fromResource(R.drawable.sign))));

        }
    }

}
