package com.example.walkholic.DataClass.Response;

import com.example.walkholic.DataClass.Data.RoadPath;
import com.example.walkholic.DataClass.Data.UserRoadPath;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoadPathRes {

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("data")
    @Expose
    private List<UserRoadPath> data = null;

    public String getError() {
        return error;
    }

    public List<UserRoadPath> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "UserRoadPathRes{" +
                "error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
