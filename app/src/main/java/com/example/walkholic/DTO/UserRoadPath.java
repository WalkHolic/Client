package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoadPath {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<UserRoadSpot> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<UserRoadSpot> getData() {
        return data;
    }

    public void setData(List<UserRoadSpot> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserRoadPath{" +
                "error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}