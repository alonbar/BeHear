package com.mycompany.behear;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        double [][] points = {{
                35.21114514900006,
        31.780378620000079
        },
        {
        35.211146521000046,
                31.780369778000079
        },
        {
        35.211048552000079,
                31.78038205200005
        },
        {
        35.210865378000051,
                31.780467196000075
        },
        {
        35.210613385000045,
                31.780591206000054
        },
        {
        35.210366762000035,
                31.780723700000067
        },
        {
        35.210177373000079,
                31.780820114000051
        },
        {
        35.210024498000053,
                31.780897854000045
        },
        {
        35.209664489000033,
                31.781088166000075
        },
        {
        35.209507509000048,
                31.781161128000065
        },
        {
        35.209361140000055,
                31.781231104000028
        },
        {
        35.209332992000043,
                31.781244561000051
        },
        {
        35.209185182000056,
                31.781325186000061
        },
        {
        35.209136835000038,
                31.781343224000068
        },
        {
        35.209018902000025,
                31.781388867000032
        },
        {
        35.208965284000044,
                31.781428274000064
        },
        {
        35.20891819700006,
                31.78148077000003
        },
        {
        35.20878627500008,
                31.781545729000072
        },
        {
        35.208594592000054,
                31.781640135000032
        },
        {
        35.208596401000079,
                31.781669717000057
        },
        {
        35.20857972500005,
                31.781837548000055
        },
        {
        35.208538770000075,
                31.782043164000072
        },
        {
        35.208526429000074,
                31.782107014000076
        },
        {
        35.208486525000069,
                31.782403456000054
        },
        {
        35.208477879000043,
                31.782503827000028
        },
        {
        35.20845044400005,
                31.782815959000061
        },
        {
        35.208438938000029,
                31.782947899000078
        },
        {
        35.208433974000059,
                31.782965930000046
        },
        {
        35.208419405000029,
                31.783016888000077
        },
        {
        35.208480646000055,
                31.783016702000054
        },
        {
        35.208498170000041,
                31.783016161000035
        },
        {
        35.208518651000077,
                31.783013638000057
        },
        {
        35.208611766000047,
                31.782998120000059
        },
        {
        35.208624230000055,
                31.782994157000076
        },
        {
        35.208654850000073,
                31.782985679000035
        },
        {
        35.208710590000067,
                31.782958035000036
        },
        {
        35.208718508000061,
                31.782954112000027
        },
        {
        35.20872577800003,
                31.782951220000029
        },
        {
        35.208849427000075,
                31.782902246000049
        },
        {
        35.20897674400004,
                31.782851558000061
        },
        {
        35.209059000000025,
                31.782809806000046
        },
        {
        35.209259159000055,
                31.782708251000031
        },
        {
        35.209317652000038,
                31.782678482000051
        },
        {
        35.209330863000048,
                31.782671522000044
        },
        {
        35.209519832000069,
                31.78257196900006
        },
        {
        35.209557218000043,
                31.782550794000031
        },
        {
        35.209646940000027,
                31.782499815000051
        },
        {
        35.20972981500006,
                31.782468965000078
        },
        {
        35.209949621000078,
                31.782347571000059
        },
        {
        35.209970843000065,
                31.782334766000076
        },
        {
        35.210177461000058,
                31.782223558000055
        },
        {
        35.210411510000029,
                31.782095581000078
        },
        {
        35.210656654000047,
                31.781959832000041
        },
        {
        35.210707099000047,
                31.781935066000074
        },
        {
        35.210894516000053,
                31.781843029000072
        },
        {
        35.211112941000067,
                31.781727500000045
        },
        {
        35.211149676000048,
                31.781713434000039
        },
        {
        35.211299494000059,
                31.781655348000072
        },
        {
        35.211398630000076,
                31.781612408000058
        },
        {
        35.211324390000073,
                31.78148913900003
        },
        {
        35.211243214000035,
                31.781381509000028
        },
        {
        35.211218488000043,
                31.781348726000033
        },
        {
        35.211114172000066,
                31.781205065000051
        },
        {
        35.211067821000029,
                31.781135714000072
        },
        {
        35.211053463000042,
                31.781114251000076
        },
        {
        35.211037196000063,
                31.781060376000028
        },
        {
        35.211032875000058,
                31.781046073000027
        },
        {
        35.211064530000044,
                31.780890498000076
        },
        {
        35.211099041000068,
                31.780678035000051
        },
        {
        35.211106077000068,
                31.780635093000058
        },
        {
        35.211124264000034,
                31.780524180000043
        },
        {
        35.21114514900006,
                31.780378620000079
        }};

//        int i = 0;
//        ArrayList<Point> table = new ArrayList<>();
//        for (i = 0; i < points.length; i++) {
//            table.add(new Point(points[i][0], points[i][1]));
//        }

//        Polygon poliy = new Polygon(table);
//        Point point = new Point(35.209912,31.78416);
//        boolean flag = poliy.isPointInPolygon(point);
//        System.out.print(flag);


        String json = null;

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
            HashMap<Integer,Polygon> polygonTable = new HashMap<>();
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
            in = this.getAssets().open("Kalpi.csv");
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


