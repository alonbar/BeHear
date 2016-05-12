package com.mycompany.behear;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.mycompany.behear.MainActivity;
/**
 * Created by baralon on 04/05/2016.
 */
public class Manager {

    Context context;
    MapHelper mapHelper;
    SoundManager soundManager;

    public Manager(Context context) {
        mapHelper = new MapHelper(context);
        mapHelper.init();
        soundManager = new SoundManager(context);

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
            Log.d("bla", ex.getMessage());
            return;
        }
        try {
            JSONObject obj = new JSONObject(json);

            for(int i = 0; i < obj.getJSONArray("features").length(); i++){
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
            reader = new BufferedReader(new InputStreamReader(in));
            reader.readLine();
            HashMap<Integer, ArrayList<Kalpi>> kapliTable = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                String[] parsedLine = line.split(",");
                Point pnt = new Point(Double.parseDouble(parsedLine[1]), Double.parseDouble(parsedLine[0]));
                if (parsedLine[2].equals("NULL"))
                    continue;
                if (kapliTable.get(Integer.valueOf(parsedLine[2])) == null) {
                    ArrayList<Kalpi> kalpiList = new ArrayList<>();
                    kalpiList.add(new Kalpi(pnt, parsedLine[3]));
                    kapliTable.put(Integer.valueOf(parsedLine[2]), kalpiList);
                }
                else {
                    kapliTable.get(Integer.valueOf(parsedLine[2])).add(new Kalpi(pnt, parsedLine[3]));
                }
            }

            for(Integer statNumber: kapliTable.keySet()) {
                StatArea area = MainActivity.statAreaTable.get(statNumber);
                if (area != null) {
                    area.setKapliList(kapliTable.get(statNumber));
                }
            }

//            while ((line = reader.readLine()) != null) {
//                String[] parsedLine = line.split(",");
//                Point pnt = new Point(Double.parseDouble(parsedLine[1]), Double.parseDouble(parsedLine[0]));
//                for(StatArea curStatArea: MainActivity.statAreaTable.values()) {
//                    if (curStatArea.getPolygon().isPointInPolygon(pnt)){
//                        curStatArea.setKalpiList(new Kalpi(pnt, parsedLine[3]));
//                        break;
//                    }
//                }
//            }
        }
        catch (Exception e){
            Log.d("bla", e.getMessage());
            return;
        }
    }


    private StatArea getStatArea(Point point) {
        Point point2 = new Point(35.210596,31.779684);
        for (StatArea curStat : MainActivity.statAreaTable.values()) {
            if (curStat.getPolygon().isPointInPolygon(point2)) {
                return curStat;
            }
        }
        return null;
    }

    public Point getCurrentCoordinate() {
        return this.mapHelper.getCurrentCooredinate();
    }

    public void startLifeCycle() {
        Point pnt = getCurrentCoordinate();
        StatArea curretArea = this.getStatArea(pnt);
        if (curretArea != null) {
            String currentPart = curretArea.getClosestKalpi(pnt);
        }

    }
}
