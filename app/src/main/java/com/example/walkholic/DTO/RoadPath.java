package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoadPath {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("roadId")
    @Expose
    private Integer roadId;
    @SerializedName("spotName")
    @Expose
    private String spotName;
    @SerializedName("roadAddr")
    @Expose
    private String roadAddr;
    @SerializedName("lotAddr")
    @Expose
    private String lotAddr;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoadId() {
        return roadId;
    }

    public void setRoadId(Integer roadId) {
        this.roadId = roadId;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getRoadAddr() {
        return roadAddr;
    }

    public void setRoadAddr(String roadAddr) {
        this.roadAddr = roadAddr;
    }

    public String getLotAddr() {
        return lotAddr;
    }

    public void setLotAddr(String lotAddr) {
        this.lotAddr = lotAddr;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "RoadSpot{" +
                "id=" + id +
                ", roadId=" + roadId +
                ", spotName='" + spotName + '\'' +
                ", roadAddr='" + roadAddr + '\'' +
                ", lotAddr='" + lotAddr + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
