package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkRes {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<ParkInfo> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<ParkInfo> getData() {
        return data;
    }

    public void setData(List<ParkInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ParkList{" +
                "error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}