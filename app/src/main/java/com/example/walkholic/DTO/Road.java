package com.example.walkholic.DTO;


public class Road {

    private int id;
    private String road_name;
    private String road_desc;
    private String start_lat;
    private String start_lng;

    public int getId() {
        return id;
    }

    public String getRoad_name() {
        return road_name;
    }

    public String getRoad_desc() {
        return road_desc;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public String getStart_lng() {
        return start_lng;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public void setRoad_desc(String road_desc) {
        this.road_desc = road_desc;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public void setStart_lng(String start_lng) {
        this.start_lng = start_lng;
    }

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", road_name='" + road_name + '\'' +
                ", road_desc='" + road_desc + '\'' +
                ", start_lat='" + start_lat + '\'' +
                ", start_lng='" + start_lng + '\'' +
                '}';
    }
}

