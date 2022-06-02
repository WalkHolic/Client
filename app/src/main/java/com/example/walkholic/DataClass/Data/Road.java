package com.example.walkholic.DataClass.Data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Road {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("roadName")
    @Expose
    private String roadName;
    @SerializedName("roadDesc")
    @Expose
    private String roadDesc;
    @SerializedName("picturePath")
    @Expose
    private String picturePath;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("startName")
    @Expose
    private String startName;
    @SerializedName("startRoadAddr")
    @Expose
    private String startRoadAddr;
    @SerializedName("startLotAddr")
    @Expose
    private String startLotAddr;
    @SerializedName("startLat")
    @Expose
    private Double startLat;
    @SerializedName("startLng")
    @Expose
    private Double startLng;
    @SerializedName("roadPathStr")
    @Expose
    private String roadPathStr;
    @SerializedName("agencyTel")
    @Expose
    private String agencyTel;
    @SerializedName("agencyName")
    @Expose
    private String agencyName;
    @SerializedName("providerName")
    @Expose
    private String providerName;
    @SerializedName("hashtag")
    @Expose
    private List<String> hashtag = null;

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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
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

    public String getRoadPathStr() {
        return roadPathStr;
    }

    public void setRoadPathStr(String roadPathStr) {
        this.roadPathStr = roadPathStr;
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

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", roadName='" + roadName + '\'' +
                ", roadDesc='" + roadDesc + '\'' +
                ", picturePath=" + picturePath +
                ", distance=" + distance +
                ", time='" + time + '\'' +
                ", startName='" + startName + '\'' +
                ", startRoadAddr='" + startRoadAddr + '\'' +
                ", startLotAddr='" + startLotAddr + '\'' +
                ", startLat=" + startLat +
                ", startLng=" + startLng +
                ", roadPathStr='" + roadPathStr + '\'' +
                ", agencyTel='" + agencyTel + '\'' +
                ", agencyName='" + agencyName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", hashtag=" + hashtag +
                '}';
    }
}

