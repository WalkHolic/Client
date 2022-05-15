package com.example.walkholic;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;

public class Trail {
    TMapData tmapdata = new TMapData();
    public String name = "새 경로";
    public ArrayList<TMapPoint> coorList = new ArrayList<TMapPoint>();
    public Double totalDistance = 0.0;
    public String totalDistString = "";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void calTotalDistance() {
        //거리계산
        for(int i = 0 ; i < coorList.size()-1 ; i++ ){
            totalDistance += distance(coorList.get(i).getLatitude(),coorList.get(i).getLongitude(),coorList.get(i+1).getLatitude(),coorList.get(i+1).getLongitude(),"kilometer");
        }
        totalDistString = String.format("%.3f",totalDistance);
    }
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        Double distTrim;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }
        distTrim = Math.round(dist*10000)/10000.0;
        return (distTrim);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
