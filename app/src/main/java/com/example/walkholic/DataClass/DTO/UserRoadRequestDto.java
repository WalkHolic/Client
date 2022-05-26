package com.example.walkholic.DataClass.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoadRequestDto {

    @Expose
    @SerializedName("trailName")
    private String trailName;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("distance")
    private Double distance;

    @Expose
    @SerializedName("startAddr")
    private String startAddr;

    @Expose
    @SerializedName("trailPoints")
    private List<List<Double>> trailPoints;

    @Expose
    @SerializedName("hashtag")
    private List<String> hashtag;

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
        return "UserRoadRequestDto{" +
                "trailName='" + trailName + '\'' +
                ", description='" + description + '\'' +
                ", distance=" + distance +
                ", startAddr='" + startAddr + '\'' +
                ", trailPoints=" + trailPoints +
                ", hashtag=" + hashtag +
                '}';
    }
}
