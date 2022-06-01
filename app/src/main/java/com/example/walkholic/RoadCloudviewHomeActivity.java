package com.example.walkholic;

import android.annotation.SuppressLint;
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
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.RoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadCloudviewHomeActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_road_home;
    Button btn_road_path;

    ImageView roadimageview;

    String picturePath;
    String roadDesc;
    String roadName;
    String agencyName;
    Double roadDistance;
    String agencyTel;
    List<String> hashtagList;
    String providerName;
    String roadPathStr;
    Double startLat;
    Double startLng;
    String time;
    String startLotAddr;
    String startRoadAddr;
    String distance;

    TextView txt_name;
    TextView txt_hashtag;
    TextView txt_road_distance;
    TextView txt_road_step;
    TextView txt_road_time;
    TextView txt_agencyTel;
    TextView txt_agencyName;
    TextView txt_addrNew;
    TextView txt_addr;
    TextView txt_distance;
    TextView txt_road_description;
    TextView txt_road_path;

    int roadId;

    private RoadRes roadRes;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road_cloudview_home);

        Intent intent = getIntent();
        roadId = intent.getIntExtra("roadId", roadId);
        double mlat = intent.getDoubleExtra("lat", 37.2844252); // default: 아주대 위경도
        double mlon = intent.getDoubleExtra("lng", 127.043568);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);
        btn_road_path = findViewById(R.id.btn_road_path);
        btn_road_home = findViewById(R.id.btn_road_home);
        roadimageview = (ImageView) findViewById(R.id.roadimageView);

        txt_name = findViewById(R.id.txt_name);
        txt_hashtag = findViewById(R.id.txt_hashtag);
        txt_road_distance = findViewById(R.id.txt_road_distance);
        txt_road_step = findViewById(R.id.txt_road_step);
        txt_road_time = findViewById(R.id.txt_road_time);
        txt_agencyTel = findViewById(R.id.txt_agencyTel);
        txt_agencyName = findViewById(R.id.txt_agencyName);
        txt_addrNew = findViewById(R.id.txt_addrNew);
        txt_addr = findViewById(R.id.txt_addr);
        txt_distance = findViewById(R.id.txt_distance);
        txt_road_description = findViewById(R.id.txt_road_description);
        txt_road_path = findViewById(R.id.txt_road_path);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_road_path.setOnClickListener(this);
        btn_road_home.setOnClickListener(this);

        getRoadById(roadId);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                roadName = roadRes.getData().get(0).getRoadName();
                roadDesc = roadRes.getData().get(0).getRoadDesc();
                time = roadRes.getData().get(0).getTime();
                roadDistance = roadRes.getData().get(0).getDistance();
                hashtagList = roadRes.getData().get(0).getHashtag();
                agencyName = roadRes.getData().get(0).getAgencyName();
                agencyTel = roadRes.getData().get(0).getAgencyTel();
                providerName = roadRes.getData().get(0).getProviderName();
                startLat = roadRes.getData().get(0).getStartLat();
                startLng = roadRes.getData().get(0).getStartLng();
                roadPathStr = roadRes.getData().get(0).getRoadPathStr();
                startLotAddr = roadRes.getData().get(0).getStartLotAddr();
                startRoadAddr = roadRes.getData().get(0).getStartRoadAddr();
                picturePath = roadRes.getData().get(0).getPicturePath();

                double twoDistance = twoDistance(startLat, startLng, mlat, mlon, "km");
                distance = "해당 위치로부터 떨어진 거리: " + String.valueOf(twoDistance) + " km";

                Log.d(TAG, "정보확인 : " + roadName + roadDesc + time + roadDistance + hashtagList + agencyName + agencyTel + providerName + startRoadAddr + startLotAddr + startLat + startLng + roadPathStr + picturePath + distance);

                if (picturePath != null) {
                    Glide.with(getApplicationContext()).load(picturePath).into(roadimageview);
                }
                String showHashtags = "";
                if (hashtagList != null && !hashtagList.isEmpty()) {
                    for (String hashtag : hashtagList) {
                        showHashtags += "#" + hashtag + " ";
                    }
                }
                int roadSteps = (int) (roadDistance / 0.00063); // 걸음 수 계산 (km 단위)
                txt_name.setText(roadName);
                txt_road_description.setText(roadDesc);
                txt_road_time.setText(time);
                txt_road_distance.setText(String.valueOf(roadDistance) + " km");
                txt_hashtag.setText(showHashtags);
                txt_agencyName.setText(agencyName);
                txt_agencyTel.setText(agencyTel);
                txt_road_step.setText(String.valueOf(roadSteps) + " 걸음");
                txt_road_path.setText(roadPathStr);
                txt_addr.setText(startLotAddr);
                txt_addrNew.setText(startRoadAddr);
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
//            case R.id.btn_road_home:
//                Intent intent5 = new Intent(this, RoadCloudviewHomeActivity.class);
//                intent5.putExtra("roadId", roadId);
//                startActivity(intent5);
//                finish();
//                break;
            case R.id.btn_road_path:
                Intent intent6 = new Intent(this, RoadCloudviewPathActivity.class);
                intent6.putExtra("roadId", roadId);
                intent6.putExtra("lat", startLat);
                intent6.putExtra("lng", startLng);
                startActivity(intent6);
                finish();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getRoadById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getRoadById(id).enqueue(new Callback<RoadRes>() {
            @Override
            public void onResponse(Call<RoadRes> call, Response<RoadRes> response) {
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
}