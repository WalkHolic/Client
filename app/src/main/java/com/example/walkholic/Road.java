package com.example.walkholic;


public class Road {

    private int id;
    private String road_name;
    private String road_desc;
    private String start_lat;
    private String start_lng;

//    private int distance;
//    private String time;
//    private String start_name;
//    private String start_road_addr;
//    private String start_lot_addr;
//    private String end_name;
//    private String end_road_addr;
//    private String end_lot_addr;
//    private String end_lat;
//    private String end_lng;
//    private String road_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoad_name() {
        return road_name;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public String getRoad_desc() {
        return road_desc;
    }

    public void setRoad_desc(String road_desc) {
        this.road_desc = road_desc;
    }

//    public int getDistance() {
//        return distance;
//    }
//
//    public void setDistance(int distance) {
//        this.distance = distance;
//    }

    //    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getStart_name() {
//        return start_name;
//    }
//
//    public void setStart_name(String start_name) {
//        this.start_name = start_name;
//    }
//
//    public String getStart_road_addr() {
//        return start_road_addr;
//    }
//
//    public void setStart_road_addr(String start_road_addr) {
//        this.start_road_addr = start_road_addr;
//    }
//
//    public String getStart_lot_addr() {
//        return start_lot_addr;
//    }
//
//    public void setStart_lot_addr(String start_lot_addr) {
//        this.start_lot_addr = start_lot_addr;
//    }
//
    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getStart_lng() {
        return start_lng;
    }

    public void setStart_lng(String start_lng) {
        this.start_lng = start_lng;
    }
//
//    public String getEnd_name() {
//        return end_name;
//    }
//
//    public void setEnd_name(String end_name) {
//        this.end_name = end_name;
//    }
//
//    public String getEnd_road_addr() {
//        return end_road_addr;
//    }
//
//    public void setEnd_road_addr(String end_road_addr) {
//        this.end_road_addr = end_road_addr;
//    }
//
//    public String getEnd_lot_addr() {
//        return end_lot_addr;
//    }
//
//    public void setEnd_lot_addr(String end_lot_addr) {
//        this.end_lot_addr = end_lot_addr;
//    }
//
//    public String getEnd_lat() {
//        return end_lat;
//    }
//
//    public void setEnd_lat(String end_lat) {
//        this.end_lat = end_lat;
//    }
//
//    public String getEnd_lng() {
//        return end_lng;
//    }
//
//    public void setEnd_lng(String end_lng) {
//        this.end_lng = end_lng;
//    }
//
//    public String getRoad_path() {
//        return road_path;
//    }
//
//    public void setRoad_path(String road_path) {
//        this.road_path = road_path;
//    }




    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", road_name='" + road_name + '\'' +
                ", road_desc=" + road_desc + '\'' +
                ", start_lat='" + start_lat + '\'' +
                ", start_lng=" + start_lng +
                '}';
    }
}

