package com.mycompany.behear;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycompany.behear.MainActivity;

import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by baralon on 04/05/2016.
 */
public class Manager {

    Context context;
    MapHelper mapHelper;
    public SoundManager soundManager;
    static boolean votesBoxFlag;
    static boolean econBoxFlag;
    static boolean eduBoxFlag;

    public Manager(Context context) {
        mapHelper = new MapHelper(context);
        mapHelper.init();
        soundManager = new SoundManager(context);
        this.context = context;

    }

    public static void BeHearInit(Context context) {
        String json = null;
        MainActivity.statAreaTable = new HashMap<>();

        try {

            InputStream is = context.getAssets().open("json2.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            //ex.printStackTrace();
            return;
        }
        try {
            JSONObject obj = new JSONObject(json);

            for (int i = 0; i < obj.getJSONArray("features").length(); i++) {
                ArrayList<Point> geometry = new ArrayList<>();
                for (int j = 0; j < obj.getJSONArray("features").getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).length(); j++) {
                    geometry.add(new Point(obj.getJSONArray("features").getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).getJSONArray(j).getDouble(0),
                            obj.getJSONArray("features").getJSONObject(i).getJSONObject("geometry").getJSONArray("rings").getJSONArray(0).getJSONArray(j).getDouble(1)));
                }
                MainActivity.statAreaTable.put(new Integer(obj.getJSONArray("features").getJSONObject(i).getJSONObject("attributes").getInt("STAT08")),
                        new StatArea(new Polygon(geometry), obj.getJSONArray("features").getJSONObject(i).getJSONObject("attributes").getInt("STAT08")));
            }

            //set the party data
            InputStream in;
            BufferedReader reader;
            String line;
            in = context.getAssets().open("Kalpi.csv");
            StatArea.kalpiList = new ArrayList<>();
            reader = new BufferedReader(new InputStreamReader(in));
            reader.readLine();
            HashMap<Integer, ArrayList<Kalpi>> kapliTable = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                String[] parsedLine = line.split(",");
                Point pnt = new Point(Double.parseDouble(parsedLine[1]), Double.parseDouble(parsedLine[0]));
                if (parsedLine[2].equals("NULL"))
                    continue;
                StatArea.kalpiList.add(new Kalpi(pnt, parsedLine[3]));
            }
        } catch (Exception e) {

            return;
        }

        try {
            InputStream in;
            BufferedReader reader;
            String line;

            //getting econ status
            in = context.getAssets().open("economy.csv");
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                String[] parsedLine = line.split(",");
                MainActivity.statAreaTable.get(Integer.valueOf(parsedLine[0])).setEcon(Integer.valueOf(parsedLine[1]));
            }

            //getting education status
            in = context.getAssets().open("edu.csv");
            reader = new BufferedReader(new InputStreamReader(in));
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll(",,," , ",***,***,");
                line = line.replaceAll(",," , ",***,");
                String[] parsedLine = line.split(",");
                if (MainActivity.statAreaTable.containsKey(Integer.valueOf(parsedLine[0])) && !(parsedLine[1].equals("***"))) {
                    MainActivity.statAreaTable.get(Integer.valueOf(parsedLine[0])).setUniGraduate(Double.valueOf(parsedLine[1]));
                }
                if (MainActivity.statAreaTable.containsKey(Integer.valueOf(parsedLine[0])) && !(parsedLine[2].equals("***"))) {
                    MainActivity.statAreaTable.get(Integer.valueOf(parsedLine[0])).setSchoolGraduate(Double.valueOf(parsedLine[2]));
                }
            }

            in = context.getAssets().open("crime.csv");
            reader = new BufferedReader(new InputStreamReader(in));
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parsedLine = line.split(",");
                if (MainActivity.statAreaTable.containsKey(Integer.valueOf(parsedLine[0]))) {
                    StatArea currentArea = MainActivity.statAreaTable.get(Integer.valueOf(parsedLine[0]));
                    if (parsedLine.length > 2) {
                        if (parsedLine[1].equals("Property_crime")) {
                            currentArea.setPropertyCrimeCount(Integer.valueOf(parsedLine[2]));
                        }
                        else if(parsedLine[1].equals("Sexua_harassment")) {
                            currentArea.setSexualHarassmentCount(Integer.valueOf(parsedLine[2]));
                        }
                        else if (parsedLine[1].equals("Violence1") || parsedLine[1].equals("Violence2")) {
                            currentArea.setViolenceCount(currentArea.getViolenceCount() + Integer.valueOf(parsedLine[2]));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return;
        }

    }


    private StatArea getStatArea(Point point) {
        if (MainActivity.statAreaTable == null)
            return null;
        for (StatArea curStat : MainActivity.statAreaTable.values()) {
            if (curStat.getPolygon().isPointInPolygon(point)) {
                return curStat;
            }
        }
        return null;
    }

    public Point getCurrentCoordinate() {
        return this.mapHelper.getCurrentCooredinate();
    }

    public void startLifeCycle(Point pnt) {
        StatArea curretArea = this.getStatArea(pnt);
        if (curretArea != null) {
            int currentEconStatus = curretArea.getEcon();
            String currentParty = curretArea.getClosestKalpi(pnt).toLowerCase();

            if (votesBoxFlag && !currentParty.equals(""))
                soundManager.playSound(Parameters.politics, currentParty.hashCode());
            if (econBoxFlag && currentEconStatus != -1) {
                    int newStatus = 0;
                    if (currentEconStatus <= 3) {
                        newStatus = 4;
                    } else if (currentEconStatus >= 4 && currentEconStatus <= 7) {
                        newStatus = 3;
                    } else if (currentEconStatus >= 8 && currentEconStatus <= 11) {
                        newStatus = 2;
                    } else if (currentEconStatus >= 12 && currentEconStatus <= 15) {
                        newStatus = 1;
                    } else if (currentEconStatus >= 16 && currentEconStatus <= 19) {
                        newStatus = 0;
                    }
                    if (econBoxFlag)
                        soundManager.playSound(Parameters.econ, newStatus);
            }

            if (!votesBoxFlag)
                soundManager.stopSound(Parameters.politics);
            if (!econBoxFlag)
                soundManager.stopSound(Parameters.econ);
            if (!eduBoxFlag)
                soundManager.stopSound(Parameters.education);
        }
        else {
            soundManager.stopSound(Parameters.politics);
            soundManager.stopSound(Parameters.econ);
            new Thread() {
                public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "The application requires you to be in Jerusalem",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }}.start();

        }
    }
}