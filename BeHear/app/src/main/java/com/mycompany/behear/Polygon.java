package com.mycompany.behear;

import java.util.ArrayList;

/**
 * Created by baralon on 06/04/2016.
 */
public class Polygon {

    private ArrayList<Point> geometryTable;
    public Polygon(ArrayList<Point> geometry) {
        this.geometryTable =  new ArrayList<Point>(geometry);
    }

    public boolean isPointInPolygon(Point p) {
        int i = 0, j = 0;
        boolean result = false;
        for (i = 0, j = geometryTable.size() - 1; i < geometryTable.size(); j = i++) {
            if ((geometryTable.get(i).getLat() > p.getLat()) != (geometryTable.get(j).getLat()> p.getLat()) &&
                    (p.getLong()< (geometryTable.get(j).getLong() - geometryTable.get(i).getLong()) * (p.getLat() - geometryTable.get(i).getLat()) / (geometryTable.get(j).getLat()- geometryTable.get(i).getLat()) + geometryTable.get(i).getLong())) {
                result = !result;
            }
        }
        return result;
    }

    public ArrayList<Point> getGeometryTable(){
        return this.geometryTable;
    }

}