package com.example.walkholic;

import com.example.walkholic.DTO.Road;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface SeverRequestApi {
    @GET("road/all")
    Call<List<Road>> getRoad();

    @POST("/auth/google")
    Call<ResponseBody> post_posts(@Body RequestBody token);
}

