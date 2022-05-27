package com.example.walkholic.DataClass.Response;

import com.example.walkholic.DataClass.Data.UserRoad;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoadSharedRes {

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("data")
    @Expose
    private List<String> data = null;

}
