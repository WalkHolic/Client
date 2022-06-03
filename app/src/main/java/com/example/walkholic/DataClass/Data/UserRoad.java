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
    @SerializedName("startLat")
    @Expose
    private Double startLat;
    @SerializedName("startLng")
    @Expose
    private Double startLng;
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

    public Object getUserId() {
        return userId;
    }

    public String getTrailName() {
        return trailName;
    }

    public String getDescription() {
        return description;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getStartLat() {
        return startLat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public String getPicture() {
        return picture;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public Boolean getShared() {
        return shared;
    }

    @Override
    public String toString() {
        return "UserRoad{" +
                "id=" + id +
                ", userId=" + userId +
                ", trailName='" + trailName + '\'' +
                ", description='" + description + '\'' +
                ", distance=" + distance +
                ", startLat=" + startLat +
                ", startLng=" + startLng +
                ", startAddr='" + startAddr + '\'' +
                ", picture='" + picture + '\'' +
                ", hashtag=" + hashtag +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", shared=" + shared +
                '}';
    }
}