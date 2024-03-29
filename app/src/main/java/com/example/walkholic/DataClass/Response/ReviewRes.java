package com.example.walkholic.DataClass.Response;

import com.example.walkholic.DataClass.Data.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewRes {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<Review> data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Review> getData() {
        return data;
    }

    public void setData(List<Review> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReviewList{" +
                "error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}