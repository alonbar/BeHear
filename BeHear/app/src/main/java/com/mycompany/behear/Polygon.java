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

    public Point getCenter(){
        double x1, x2, y1, y2, A, Cx, Cy;
        A = 0; Cx = 0; Cy = 0;
        for(int i = 0; i < geometryTable.size() - 1; i++){
            x1 = geometryTable.get(i).getLong();
            y1 = geometryTable.get(i).getLat();
            x2 = geometryTable.get(i+1).getLong();
            y2 = geometryTable.get(i+1).getLat();
            A += x1 * y2 - x2 * y1;
            Cx += (x1 + x2) * (x1 * y2 - x2 * y1);
            Cy += (y1 + y2) * (x1 * y2 - x2 * y1);
        }
        A = 0.5 * A;
        Cx = (1/(6 * A)) * Cx;
        Cy = (1/(6 * A)) * Cy;
        return new Point(Cx, Cy);
    }

    public ArrayList<Point> getGeometryTable(){
        return this.geometryTable;
    }

}