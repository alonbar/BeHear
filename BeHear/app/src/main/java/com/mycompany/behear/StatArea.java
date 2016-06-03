package com.mycompany.behear;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Inbar on 04/05/2016.
 */
public class StatArea {
    private Polygon polygon;
    private ArrayList<Kalpi> kalpiList; //key = name of miflaga, value = num of vots
    private int socio;
    private int id;
    private double schoolGraduate;
    private double uniGraduate;
    public StatArea(Polygon polygon, int id){
        this.polygon = new Polygon(polygon.getGeometryTable());
        this.socio = -1;
        this.schoolGraduate = -1;
        this.uniGraduate = -1;
        this.kalpiList = new ArrayList<>();
        this.id = id;
    }

    public StatArea(){
        this.polygon = new Polygon(polygon.getGeometryTable());
        this.socio = -1;
        this.kalpiList = new ArrayList<>();
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

    public void addKalpiToList(Kalpi kalpi){this.kalpiList.add(kalpi);}

    public Polygon getPolygon(){return this.polygon;}

    public ArrayList<Kalpi> getKalpiList(){return this.kalpiList;}

    public String getClosestKalpi(Point point){
        double minDistance = Double.MAX_VALUE;
        String party = "";
        if(this.kalpiList.isEmpty()){
            return "";
        }
        for(int i = 0; i < this.kalpiList.size(); i++){
            double distance = this.distance(point.getLat(), point.getLong(), this.kalpiList.get(i).getPoint().getLat(), this.kalpiList.get(i).getPoint().getLong(), 'K');
            if(minDistance > distance){
                minDistance = distance;
                party = this.kalpiList.get(i).getPopolarParty();
            }
        }
        return party;
    }

    public void setKapliList(ArrayList<Kalpi> list) {
        for (Kalpi item: list) {
            this.kalpiList.add(new Kalpi(item));
        }
    }


    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  taken from http://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi:*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(Math.min(dist,1));;
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void setEcon(int status) {
        this.socio = status;
    }

    public int getEcon(){
        return this.socio;
    }

    public double getSchoolGraduate() {
        return this.schoolGraduate;
    }

    public void setSchoolGraduate(double precentage) {
        this.schoolGraduate = precentage;
    }

    public double getUniGraduate() {
        return this.uniGraduate;
    }

    public void setUniGraduate(double precentage) {
        this.uniGraduate = precentage;
    }


}
