package com.example.walkholic.Service;

import com.example.walkholic.DataClass.DTO.UserRoadUpdateRequestDto;
import com.example.walkholic.DataClass.Data.ParkOption;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.RoadRes;
import com.example.walkholic.DataClass.Response.RoadPathRes;
import com.example.walkholic.DataClass.Response.UserRes;
import com.example.walkholic.DataClass.DTO.UserRoadRequestDto;
import com.example.walkholic.DataClass.Response.UserRoadPathRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.DataClass.Response.UserRoadSharedRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ServerRequestApi {

    /* Park (공원) */
    @GET("/park/{id}")
    // park id 로 공원 조회
    Call<ParkRes> getParkById(@Path("id") int id);

    @GET("/park/nearParks")
        // 근처 공원 조희
    Call<ParkRes> getParkByCurrentLocation(@Query("lat") double lat, @Query("lng") double lng);

    @POST("/park/filter")
        // 조건에 맞는 공원 검색
    Call<ParkRes> getParkByFilter(@Query("lat") double lat, @Query("lng") double lng, @Body ParkOption option);

    // 리뷰 관련
    @Multipart
    @POST("/park/{id}/review")
    // 공원 리뷰 작성
    Call<ReviewRes> uploadParkReview(@Path("id") int id,
                                     @Part("reviewRequestDto") RequestBody reviewRequestDto,
                                     @Part MultipartBody.Part file);

    @GET("/park/{id}/review")
        // 공원 리뷰 보기
    Call<ReviewRes> getParkReview(@Path("id") int id);

    @DELETE("/park/review/{id}")
        // 공원 리뷰 삭제
    Call<ReviewRes> delParkReview(@Path("id") int id);

    @PUT("/park/review/{id}")
        // 공원 리뷰 수정
    Call<UserRoadRes> updateParkReview(@Path("id") int id);


    /* Road (기본 산책로) */
    @GET("/road/{id}")
    Call<RoadRes> getRoadById(@Path("id") int id);

    @GET("/road/path/roadId/{rid}")
        // road id 로 산책로 경로 조희
    Call<RoadPathRes> getRoadPathByRid(@Path("rid") int rid);

    @GET("/road/nearRoads")
        // 근처 산책로 조희
    Call<RoadRes> getRoadByCurrentLocation(@Query("lat") double lat, @Query("lng") double lng);

    // 리뷰 관련
    @POST("/road/{id}/review")
    // 산책로 리뷰 작성
    Call<ReviewRes> uploadRoadReview(@Path("id") int id,
                                     @Part("reviewRequestDto") RequestBody reviewRequestDto,
                                     @Part MultipartBody.Part file);

    @GET("/road/{id}/review")
        // 산책로 리뷰 보기
    Call<ReviewRes> getRoadReview(@Path("id") int id);

    @DELETE("/road/review/{id}")
        // 산책로 리뷰 삭제
    Call<ReviewRes> delRoadReview(@Path("id") int id);

    @PUT("/road/review/{id}")
        // 산책로 리뷰 수정
    Call<ReviewRes> updateRoadReview(@Path("id") int id);


    /* Shared Road (공유 산책로) */
    @GET("/user/road/hashtag")
    // 해시태그로 산책로 검색
    Call<UserRoadRes> getUserRoadByHashtag(@Query("keyword") String keyword);

    @GET("/user/road/{rid}")
        // user id로 산책로 조희
    Call<UserRoadRes> getUserRoadById(@Path("rid") int rid);

    @GET("/user/road/nearRoads")
        // 근처 공유 산책로 조희
    Call<UserRoadRes> getUserRoadByCurrentLocation(@Query("lat") double lat, @Query("lng") double lng);

    // 리뷰 관련
    @POST("/userRoad/{id}/review")
    // 공유 산책로 리뷰 작성
    Call<ReviewRes> uploadUserRoadReview(@Path("id") int id,
                                         @Part("reviewRequestDto") RequestBody reviewRequestDto,
                                         @Part MultipartBody.Part file);

    @GET("/userRoad/{id}/review")
        // 공유 산책로 리뷰 보기
    Call<ReviewRes> getUserRoadReview(@Path("id") int id);

    @DELETE("/userRoad/review/{id}")
        // 공유 산책로 리뷰 삭제
    Call<ReviewRes> delUserRoadReview(@Path("id") int id);

    @PUT("/userRoad/review/{id}")
        // 공유 산책로 리뷰 수정
    Call<ReviewRes> updateUserRoadReview(@Path("id") int id);


    /* My Information (마이페이지, 지도) */
    @POST("/user/road")
    // 산책로 생성
    Call<UserRoadRes> createMyRoad(@Body UserRoadRequestDto roadRequestDto);

    @GET("/user/road")
        // 내 산책로 조희
    Call<UserRoadRes> getMyRoad();

    @GET("/user/road/{id}/paths")
        // 공유 산책로 경로 조회
    Call<UserRoadPathRes> getUserRoadPathById(@Path("id") int id);

    @DELETE("/user/road/{rid}")
        // 내 산책로 삭제
    Call<UserRoadRes> delMyRoad(@Path("rid") int rid);

    @Multipart
    @PUT("/user/road/{rid}")
        // 내 산책로 수정
    Call<UserRoadRes> updateMyRoad(@Path("rid") int rid,
                                   @Part("userRoadUpdateRequestDto") RequestBody userRoadUpdateRequestDto,
                                   @Part MultipartBody.Part file);

    @GET("/user/road/{rid}/share")
        // 공유 상태 변경
    Call<UserRoadSharedRes> changeShareFlag(@Path("rid") int rid);


    // Login
    @POST("/auth/google")
    // 로그인
    Call<UserRes> login(@Body RequestBody token);


}

