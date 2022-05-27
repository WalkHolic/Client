package com.example.walkholic.DataClass.Response;

import java.util.List;

import com.example.walkholic.DataClass.Data.RoadPath;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoadPathRes {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<RoadPath> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<RoadPath> getData() {
        return data;
    }

    public void setData(List<RoadPath> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RoadPath{" +
                "error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}