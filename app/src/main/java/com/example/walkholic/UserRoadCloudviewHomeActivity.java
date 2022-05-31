package com.example.walkholic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Response.RoadRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRoadCloudviewHomeActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_user_road_home;
    Button btn_user_road_path;

    ImageView userRoadImageView;

    String picturePath;
    String roadDesc;
    String roadName;
    Double roadDistance;
    List<String> hashtagList;
    Double startLat;
    Double startLng;
    String startLotAddr;
    String distance;

    TextView txt_name;
    TextView txt_hashtag;
    TextView txt_road_distance;
    TextView txt_road_step;
    TextView txt_road_time;
    TextView txt_addrNew;
    TextView txt_addr;
    TextView txt_distance;
    TextView txt_road_description;

    int userRoadId;

    private UserRoadRes userRoadRes;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_road_cloudview_home);

        Intent intent = getIntent();
        userRoadId = intent.getIntExtra("userRoadId", userRoadId);
        double mlat = intent.getDoubleExtra("lat", 37.2844252); // default: 아주대 위경도
        double mlon = intent.getDoubleExtra("lng", 127.043568);
        Log.d(TAG, "받아온 intent 값: " + userRoadId + "/" + mlat + "/" + mlon);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);

        btn_user_road_home = findViewById(R.id.btn_user_road_home);
        btn_user_road_path = findViewById(R.id.btn_user_road_path);
        userRoadImageView = (ImageView) findViewById(R.id.userRoadImageView);

        txt_name = findViewById(R.id.txt_name);
        txt_hashtag = findViewById(R.id.txt_hashtag);
        txt_road_distance = findViewById(R.id.txt_road_distance);
        txt_road_step = findViewById(R.id.txt_road_step);
        txt_road_time = findViewById(R.id.txt_road_time);
        txt_addrNew = findViewById(R.id.txt_addrNew);
        txt_addr = findViewById(R.id.txt_addr);
        txt_distance = findViewById(R.id.txt_distance);
        txt_road_description = findViewById(R.id.txt_road_description);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_user_road_home.setOnClickListener(this);
        btn_user_road_path.setOnClickListener(this);

        getUserRoadById(userRoadId);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                roadName = userRoadRes.getData().get(0).getTrailName();
                roadDesc = userRoadRes.getData().get(0).getDescription();
                roadDistance = userRoadRes.getData().get(0).getDistance();
                hashtagList = userRoadRes.getData().get(0).getHashtag();
                startLat = userRoadRes.getData().get(0).getStartLat();
                startLng = userRoadRes.getData().get(0).getStartLng();
                startLotAddr = userRoadRes.getData().get(0).getStartAddr();
                picturePath = userRoadRes.getData().get(0).getPicture();

                int roadSteps = (int) (roadDistance / 0.00063); // 걸음 수
                int time = (int) (roadDistance / 0.08); // 총 시간(분) - km 단위

                double twoDistance = twoDistance(startLat, startLng, mlat, mlon, "km");
                distance = "해당 위치로부터 떨어진 거리: " + String.valueOf(twoDistance) + " km";

                Log.d(TAG, "정보확인 : " + roadName + roadDesc + roadSteps + time + roadDistance + hashtagList + startLotAddr + startLat + startLng + picturePath + distance);

                if (picturePath != null) {
                    Glide.with(getApplicationContext()).load(picturePath).into(userRoadImageView);
                }
                String showHashtags = "";
                if (hashtagList != null && !hashtagList.isEmpty()) {
                    for (String hashtag : hashtagList) {
                        showHashtags += "#" + hashtag + " ";
                    }
                }
                txt_name.setText(roadName);
                txt_road_description.setText(roadDesc);
                txt_road_time.setText(String.valueOf(time) + " 분");
                txt_road_distance.setText(String.valueOf(roadDistance) + " km");
                txt_hashtag.setText(showHashtags);
                txt_road_step.setText(String.valueOf(roadSteps) + " 걸음");
                txt_addr.setText(startLotAddr);
                txt_distance.setText(distance);
            }
        }, 300); // 0.3초후

    }

    private static double twoDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
        Double distTrim;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }
        distTrim = Math.round(dist * 10000) / 10000.0;
        return (distTrim);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_search:
                Intent intent2 = new Intent(this, Search_ParkActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_walking:
                Intent intent3 = new Intent(this, WalkingActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.btn_mypage:
                Intent intent4 = new Intent(this, WalkListActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.btn_user_road_home:
                Intent intent5 = new Intent(this, UserRoadCloudviewHomeActivity.class);
                intent5.putExtra("userRoadId", userRoadId);
                startActivity(intent5);
                break;
//            case R.id.btn_user_road_path:
//                Intent intent6 = new Intent(this, UserRoadCloudviewPathActivity.class);
//                intent6.putExtra("userRoadId", userRoadId);
//                intent6.putExtra("lat", startLat);
//                intent6.putExtra("lng", startLng);
//                startActivity(intent6);
//                finish();
//                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getUserRoadById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getUserRoadById(id).enqueue(new Callback<UserRoadRes>() {
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
}