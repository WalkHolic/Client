package com.example.walkholic.DTO;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRoad {

    @SerializedName("trailName")
    @Expose
    private String trailName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("startAddr")
    @Expose
    private String startAddr;
    @SerializedName("trailPoints")
    @Expose
    private List<List<Double>> trailPoints = null;
    @SerializedName("hashtag")
    @Expose
    private List<String> hashtag = null;

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public List<List<Double>> getTrailPoints() {
        return trailPoints;
    }

    public void setTrailPoints(List<List<Double>> trailPoints) {
        this.trailPoints = trailPoints;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }
    @Override
    public String toString() {
        return "UserRoad{" +
                "trailName='" + trailName + '\'' +
                ", description='" + description + '\'' +
                ", distance=" + distance +
                ", startAddr='" + startAddr + '\'' +
                ", trailPoints=" + trailPoints +
                ", hashtag=" + hashtag +
                '}';
    }
}