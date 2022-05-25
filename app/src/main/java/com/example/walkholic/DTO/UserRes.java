package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRes {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<User> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "UserList{" +
                "error=" + error +
                ", data=" + data +
                '}';
    }
}