package com.mycompany.behear;

/**
 * Created by Inbar on 04/05/2016.
 */
public class Kalpi {
    private Point point;
    private String popolarParty;

    public Kalpi(Point point, String party){
        this.point = point;
        this.popolarParty = party;
    }

    public Point getPoint(){
        return this.point;
    }

    public String getPopolarParty(){
        return this.popolarParty;
    }
}
