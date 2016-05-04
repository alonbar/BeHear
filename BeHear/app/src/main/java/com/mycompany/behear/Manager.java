package com.mycompany.behear;

import android.content.Context;

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
        } catch (IOException ex) {
            ex.printStackTrace();
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
                StatArea statObj = new StatArea();
                statObj.setPolygon(new Polygon(geometry));
                MainActivity.statAreaTable.put(new Integer(obj.getJSONArray("features").getJSONObject(i).getJSONObject("attributes").getInt("STAT08")),
                        statObj);
            }

            //set the party data
            InputStream in;
            BufferedReader reader;
            String line;
            in = context.getAssets().open("Kalpi.csv");
            reader = new BufferedReader(new InputStreamReader(in));

            while ((line = reader.readLine()) != null) {
                String[] parsedLine = line.split(",");
                Point pnt = new Point(Double.parseDouble(parsedLine[0]), Double.parseDouble(parsedLine[1]));
                for (StatArea curStatArea: MainActivity.statAreaTable.values()) {
                    if (curStatArea.getPolygon().isPointInPolygon(pnt)){
                        curStatArea.setKalpiList(new Kalpi(pnt, parsedLine[3]));
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            return;
        }
    }
}
