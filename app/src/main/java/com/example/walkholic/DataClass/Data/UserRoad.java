package com.example.walkholic.DataClass.Data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRoad {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Object userId;
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
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("hashtag")
    @Expose
    private List<String> hashtag = null;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("shared")
    @Expose
    private Boolean shared;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    @Override
    public String toString() {
        return "UserRoad{" +
                "id=" + id +
                ", userId=" + userId +
                ", trailName='" + trailName + '\'' +
                ", description='" + description + '\'' +
                ", distance=" + distance +
                ", startAddr='" + startAddr + '\'' +
                ", picture='" + picture + '\'' +
                ", hashtag=" + hashtag +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", shared=" + shared +
                '}';
    }
}