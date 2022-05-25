package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoadRes {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<UserRoad> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<UserRoad> getData() {
        return data;
    }

    public void setData(List<UserRoad> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserRoadRes{" +
                "error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}