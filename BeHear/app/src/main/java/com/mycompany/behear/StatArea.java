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
        this.socio = -1;
        this.kalpiList = new ArrayList<>();
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

    public void setKalpiList(Kalpi kalpi){this.kalpiList.add(kalpi);}

    public Polygon getPolygon(){return this.polygon;}

    public ArrayList<Kalpi> getKalpiList(){return this.kalpiList;}

    public String getClosestKalpi(Point point){
        double minDistance = Double.MAX_VALUE;
        String party = "";
        if(this.kalpiList.isEmpty()){
            return "";
        }
        for(int i = 0; i < this.kalpiList.size(); i++){
            double dx = point.x - this.kalpiList.get(i).getPoint().x;
            double dy = point.y - this.kalpiList.get(i).getPoint().y;
            double distance = Math.sqrt(dx*dx + dy*dy);
            if(minDistance > distance){
                minDistance = distance;
                party = this.kalpiList.get(i).getPopolarParty();
            }
        }
        return party;
    }
}
