package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoadList {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<Road> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Road> getData() {
        return data;
    }

    public void setData(List<Road> data) {
        this.data = data;
    }
}