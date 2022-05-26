package com.example.walkholic.Service;

import android.util.Log;

import com.example.walkholic.DTO.ParkRes;
import com.example.walkholic.DTO.ReviewRequestDto;
import com.example.walkholic.DTO.ReviewRes;
import com.example.walkholic.DTO.RoadPathRes;
import com.example.walkholic.DTO.RoadRes;
import com.example.walkholic.DTO.UserRes;
import com.example.walkholic.DTO.UserRoadRequestDto;
import com.example.walkholic.DTO.UserRoadRes;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class RequestMethod{
    private ParkRes parkRes;
    private UserRoadRes userRoadRes;
    private RoadRes roadRes;
    private ReviewRes reviewRes;

    public void getParkById(int id) {
    }

    public void getParkByCurrentLocation(double lat, double lng){
        //이해찬 추가
        /////////////////////////////////////////////////////////////////////////
        final String TAG = "dlgochan";
        // 안드로이드 앱 내부 파일 (SharedPreference) 에서 jwt 값 가져오기
//        context = this;
//        String token = PreferenceManager.getString(context, "token");
//        Log.d(TAG, "onCreate Token: " + token);
        //서비스 생성 (항상 헤더에 토큰을 담아서 리퀘스트)
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        // 알맞는 request 형식 (여기서는 token) 을 파라미터로 담아서 리퀘스트
//        service.getParkByCurrentLocation(currentLat, currentLng).enqueue(new Callback<ParkList>() {
        service.getParkByCurrentLocation(lat, lng).enqueue(new Callback<ParkRes>() { // ( 여기 숫자부분에 GPS 정보 받아와서 넣어주시면 정상 작동할 것 같습니다 )
            @Override
            public void onResponse(Call<ParkRes> call, Response<ParkRes> response) { // Call<타입> : 타입을 잘 맞춰주시면 됩니다. ex) 산책로 조회는 RoadList, 산책로 경로 조회는 RoadPath
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    parkRes = response.body();
                    Log.d(TAG, "onResponse Success : " + parkRes.toString());

                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ParkRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());

            }
        });

    }

    public void getParkByFilter(double lat, double lng) {
    }

    public void uploadParkReview(int id, ReviewRequestDto reviewRequestDto, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.uploadParkReview(id, reviewRequestDto, file).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + reviewRes.toString());
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public void getParkReview(int id) {
    }

    public void delParkReview(int id) {
    }

    public void RoadById(int id) {
    }

    public void getRoadPathByRid(int rid) {
    }

    public void getUserRoadByCurrentLocation(double lat, double lng){
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getUserRoadByCurrentLocation(lat, lng).enqueue(new Callback<UserRoadRes>() { // ( 여기 숫자부분에 GPS 정보 받아와서 넣어주시면 정상 작동할 것 같습니다 )
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) { // Call<타입> : 타입을 잘 맞춰주시면 됩니다. ex) 산책로 조회는 RoadList, 산책로 경로 조회는 RoadPath
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    userRoadRes = response.body();
                    Log.d(TAG, "onResponse Success : " + userRoadRes.toString());

                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<UserRoadRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());

            }
        });

    }

    public void createMyRoad(@Body UserRoadRequestDto roadRequestDto) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.createMyRoad(roadRequestDto).enqueue(new Callback<UserRoadRes>() {
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) {
                if (response.isSuccessful()) {
                    userRoadRes = response.body();
                    Log.d(TAG, "onResponse Success : " + userRoadRes.toString());
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<UserRoadRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public Call<RoadRes> getMyRoad() {
        return null;
    }

    public Call<RoadRes> getMyRoadPath(int rid) {
        return null;
    }

    public Call<ResponseBody> delMyRoad(int rid) {
        return null;
    }

    public Call<ResponseBody> changeShareFlag(int rid) {
        return null;
    }

    public Call<UserRes> login(RequestBody token) {
        return null;
    }

    public void getRoadByCurrentLocation(double lat, double lng){
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getRoadByCurrentLocation(lat, lng).enqueue(new Callback<RoadRes>() { // ( 여기 숫자부분에 GPS 정보 받아와서 넣어주시면 정상 작동할 것 같습니다 )
            @Override
            public void onResponse(Call<RoadRes> call, Response<RoadRes> response) { // Call<타입> : 타입을 잘 맞춰주시면 됩니다. ex) 산책로 조회는 RoadList, 산책로 경로 조회는 RoadPath
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    roadRes = response.body();
                    Log.d(TAG, "onResponse Success : " + roadRes.toString());

                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<RoadRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());

            }
        });

    }


    public void uploadRoadReview(int id, RequestBody reviewJson) {
    }


    public void getRoadReview(int id) {
    }


    public void delRoadReview(int id) {
    }


    public void getUserRoadByHashtag(String keyword) {
    }


    public void getUserRoadById(int uid) {

    }

}