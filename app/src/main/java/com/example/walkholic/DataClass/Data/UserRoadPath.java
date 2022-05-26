package com.example.walkholic.DataClass.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRoadPath {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    public Integer getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "UserRoadPath{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
