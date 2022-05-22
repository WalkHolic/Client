package com.example.walkholic.DTO;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoadPath {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<RoadSpot> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<RoadSpot> getData() {
        return data;
    }

    public void setData(List<RoadSpot> data) {
        this.data = data;
    }
}