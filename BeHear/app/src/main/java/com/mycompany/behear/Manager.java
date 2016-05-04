package com.mycompany.behear;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by baralon on 04/05/2016.
 */
public class Manager {

    static private HashMap<Integer,Polygon> polygonTable = new HashMap<>();

    public static void BeHearInit(Context context) {
        String json = null;

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
                polygonTable.put(new Integer(obj.getJSONArray("features").getJSONObject(i).getJSONObject("attributes").getInt("STAT08")),
                        new Polygon(obj.getJSONArray("features").getJSONObject(i).getJSONObject("attributes").getInt("STAT08"), geometry));
            }
            InputStream in;
            BufferedReader reader;
            String line;
            in = context.getAssets().open("Kalpi.csv");
            reader = new BufferedReader(new InputStreamReader(in));
            ArrayList<String> newKapli = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Point pnt = new Point(Double.parseDouble(line.split(",")[line.split(",").length -1]), Double.parseDouble(line.split(",")[line.split(",").length -2]));
                int polyID = -1;
                for (Polygon currentPoly: polygonTable.values()) {
                    if (currentPoly.isPointInPolygon(pnt)){
                        polyID = currentPoly.getId();
                        break;
                    }
                }
                if (polyID == -1) {
                    line += ",NULL";
                }
                else {
                    line += "," + Integer.toString(polyID);
                }
                newKapli.add(line);
            }
            System.out.println(line);

        }
        catch (Exception e){
            return;
        }
    }
}
