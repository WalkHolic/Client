package com.example.walkholic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface JsonApi {
    @GET("road/all")
    Call<List<com.example.walkholic.Road>> getRoad();
}

