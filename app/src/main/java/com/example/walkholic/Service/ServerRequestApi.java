package com.example.walkholic.Service;

import com.example.walkholic.DTO.ParkRes;
import com.example.walkholic.DTO.ReviewRes;
import com.example.walkholic.DTO.RoadRes;
import com.example.walkholic.DTO.RoadPathRes;
import com.example.walkholic.DTO.UserRes;
import com.example.walkholic.DTO.UserRoadRes;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ServerRequestApi {

    // Park
    @GET("/park/{id}") // park id 로 공원 조회
    Call<ParkRes> getParkById(@Path("id") int id);

    @GET("/park/nearParks") // 근처 공원 조희
    Call<ParkRes> getParkByCurrentLocation(@Query("lat") double lat, @Query("lng") double lng);

    @POST("/park/filter") // 조건에 맞는 공원 검색
    Call<ParkRes> getParkByFilter(@Query("lat") double lat, @Query("lng") double lng);

    @POST("/park/{id}/review") // 공원 리뷰 작성
    Call<ResponseBody> uploadParkReview(@Path("id") int id, @Body RequestBody review);

    @GET("/park/{id}/review") // 공원 리뷰 보기
    Call<ReviewRes> getParkReview(@Path("id") int id);

    @DELETE("/park/review/{id}") // 공원 리뷰 삭제
    Call<ResponseBody> delParkReview(@Path("id") int id);



    // Road
    @GET("/road/path/roadId/{id}") // road id 로 산책로 조희
    Call<RoadRes> getRoadById(@Path("id") int id);

    @GET("/road/path/roadId/{rid}") // road id 로 산책로 경로 조희
    Call<RoadPathRes> getRoadPathByRid(@Path("rid") int rid);

    @GET("/road/nearRoads") // 근처 산책로 조희
    Call<ParkRes> getRoadByCurrentLocation(@Query("lat") double lat, @Query("lng") double lng);

    @POST("/road/{id}/review") // 산책로 리뷰 작성
    Call<ResponseBody> uploadRoadReview(@Path("id") int id, @Body RequestBody reviewJson);

    @GET("/road/{id}/review") // 산책로 리뷰 보기
    Call<ReviewRes> getRoadReview(@Path("id") int id);

    @DELETE("/road/review/{id}") // 산책로 리뷰 삭제
    Call<ResponseBody> delRoadReview(@Path("id") int id);




    // Shared Road
    @GET("/user/road/hashtag") // 해시태그로 산책로 검색
    Call<UserRoadRes> getUserRoadByHashtag(@Query("keyword") String keyword);

    @GET("/user/road/{uid}/paths") // user id로 산책로 조희
    Call<UserRoadRes> getUserRoadById(@Query("uid") int uid);

    @GET("/user/road/nearRoads") // 근처 공유 산책로 조희
    Call<ParkRes> getUserRoadByCurrentLocation(@Query("lat") double lat, @Query("lng") double lng);


    // My Information
    @POST("/user/road") // 산책로 생성
    Call<ResponseBody> createMyRoad(@Body RequestBody roadJson);

    @GET("/user/road") // 내 산책로 조희
    Call<RoadRes> getMyRoad();

    @GET("/user/road/{rid}/paths") // 내 산책로 경로 조회
    Call<RoadRes> getMyRoadPath(@Path("rid") int rid);

    @DELETE("/user/road/{rid}") // 내 산책로 삭제
    Call<ResponseBody> delMyRoad(@Path("rid") int rid);

    @GET("/user/road/{rid}/share") // 공유 상태 변경
    Call<ResponseBody> changeShareFlag(@Path("rid") int rid);



    // Login
    @POST("/auth/google") // 로그인
    Call<UserRes> login(@Body RequestBody token);


}

