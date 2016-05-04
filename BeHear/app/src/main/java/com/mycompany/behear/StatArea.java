package com.mycompany.behear;

import java.util.HashMap;

/**
 * Created by Inbar on 04/05/2016.
 */
public class StatArea {
    private Polygon polygon;
    private String party; //key = name of miflaga, value = num of vots
    private int socio;
    //ctor
    public StatArea(){
        this.polygon = null;
        this.party = "";
        this.socio = -1;
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

    public void setParty(String party){
        this.party = party;
    }
}
