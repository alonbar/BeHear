package com.mycompany.behear;

import java.util.ArrayList;

/**
 * Created by baralon on 06/04/2016.
 */
public class Polygon {

    private ArrayList<Point> geometryTable;
    //private int id;
    public Polygon(ArrayList<Point> geometry) {
      //  this.id = id;
        this.geometryTable =  new ArrayList<Point>(geometry);
    }

    public boolean isPointInPolygon(Point p) {
        int i = 0, j = 0;
        boolean result = false;
        for (i = 0, j = geometryTable.size() - 1; i < geometryTable.size(); j = i++) {
            if ((geometryTable.get(i).y > p.y) != (geometryTable.get(j).y > p.y) &&
                    (p.x < (geometryTable.get(j).x - geometryTable.get(i).x) * (p.y - geometryTable.get(i).y) / (geometryTable.get(j).y - geometryTable.get(i).y) + geometryTable.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

    //public int getId(){
    //    return this.id;
    //}
}