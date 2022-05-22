package com.example.walkholic.DTO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Road {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("road_name")
    @Expose
    private String roadName;
    @SerializedName("road_desc")
    @Expose
    private String roadDesc;
    @SerializedName("picture_path")
    @Expose
    private Object picturePath;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("start_name")
    @Expose
    private String startName;
    @SerializedName("start_road_addr")
    @Expose
    private String startRoadAddr;
    @SerializedName("start_lot_addr")
    @Expose
    private String startLotAddr;
    @SerializedName("start_lat")
    @Expose
    private Double startLat;
    @SerializedName("start_lng")
    @Expose
    private Double startLng;
    @SerializedName("agency_tel")
    @Expose
    private String agencyTel;
    @SerializedName("agency_name")
    @Expose
    private String agencyName;
    @SerializedName("base_date")
    @Expose
    private String baseDate;
    @SerializedName("provider_code")
    @Expose
    private String providerCode;
    @SerializedName("provider_name")
    @Expose
    private String providerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadDesc() {
        return roadDesc;
    }

    public void setRoadDesc(String roadDesc) {
        this.roadDesc = roadDesc;
    }

    public Object getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(Object picturePath) {
        this.picturePath = picturePath;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getStartRoadAddr() {
        return startRoadAddr;
    }

    public void setStartRoadAddr(String startRoadAddr) {
        this.startRoadAddr = startRoadAddr;
    }

    public String getStartLotAddr() {
        return startLotAddr;
    }

    public void setStartLotAddr(String startLotAddr) {
        this.startLotAddr = startLotAddr;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public void setStartLng(Double startLng) {
        this.startLng = startLng;
    }

    public String getAgencyTel() {
        return agencyTel;
    }

    public void setAgencyTel(String agencyTel) {
        this.agencyTel = agencyTel;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}

