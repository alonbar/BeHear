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
    //ctor
    public StatArea(){
        this.polygon = null;
        this.kalpiList = null;
        this.socio = -1;
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

    public void setKalpiList(Kalpi kalpi){
        this.kalpiList.add(kalpi);
    }

    public Polygon getPolygon(){return this.polygon;}

    public  ArrayList<Kalpi> getKalpiList(){return this.kalpiList;}
}
