package com.mycompany.behear;

/**
 * Created by baralon on 06/04/2016.
 */
//X = Longtitue y = latitude
// in jerusalem
//long looks like: 35.179394153000032,
//lat looks like 31.790121721000048

public class Point {
    private double x;
    private double y;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point (Point other) {
        this(other.x, other.y);
    }

    public double getLat() {
        return this.y;
    }

    public double getLong() {
        return this.x;
    }

    public void setLat(double lat){
        this.y = lat;
    }

    public void setLong(double lng) {
        this.x = lng;
    }
}
