package com.example.walkholic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.RoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadCloudviewReviewActivity extends AppCompatActivity implements View.OnClickListener {


    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_road_home;
    Button btn_road_review;
    Button btn_road_path;

    ListView listView;

    ImageView roadimageview;

    String picturePath;

    int roadId;

    boolean path_check = false;

    private RoadRes roadRes;
    private ReviewRes reviewRes;

    Handler mHandler = new Handler();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road_cloudview_review);

        Intent intent = getIntent();
        roadId = intent.getIntExtra("roadId", roadId);
        //double mlat = intent.getDoubleExtra("lat", 37.2844252); // default: 아주대 위경도
        //double mlon = intent.getDoubleExtra("lng", 127.043568);

        Log.d("dlgochan", "roadId 확인: " + roadId);

        roadimageview = (ImageView) findViewById(R.id.roadimageView);

        // Log.d(TAG, "풍선뷰 정보 :  " + parkInfo.getPngPath() );
        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);
        btn_road_home = findViewById(R.id.btn_road_home);
        btn_road_review = findViewById(R.id.btn_road_review);
        btn_road_path = findViewById(R.id.btn_road_path);

        listView = findViewById(R.id.road_review_list);


        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_road_home.setOnClickListener(this);
        btn_road_review.setOnClickListener(this);
        btn_road_path.setOnClickListener(this);

        ArrayList<String> usernamelist = new ArrayList<>();
        ArrayList<String> commentlist = new ArrayList<>();
        ArrayList<String> pngpathlist = new ArrayList<>();

        getRoadById(roadId);
        getRoadReview(roadId);


        mHandler.postDelayed(new Runnable() {
            public void run() {
                picturePath = roadRes.getData().get(0).getPicturePath();

                if (picturePath != null) {
                    Glide.with(getApplicationContext()).load(picturePath).into(roadimageview);
                }else{
                    roadimageview.setImageResource(R.drawable.basic_park);
                }
                if(reviewRes != null){
                    Log.d(TAG, "널 아님!");
                    for (int i = 0; i < reviewRes.getData().size(); i++) {
                        String username = reviewRes.getData().get(i).getUserName();
                        String comment = reviewRes.getData().get(i).getContent();
                        String pngpath = reviewRes.getData().get(i).getPngPath();

                        Log.d(TAG, "유저이름 : " + username);
                        Log.d(TAG, "리뷰 : " + comment);
                        Log.d(TAG, "사진경로 : " + pngpath);

                        usernamelist.add(username);
                        commentlist.add(comment);
                        pngpathlist.add(pngpath);
                    }
                    Log.d(TAG, usernamelist.toString());
                    ArrayList<ParkReviewData> reviewDatalist = new ArrayList<ParkReviewData>();
                    for (int i = 0; i < usernamelist.size(); i++) {
                        reviewDatalist.add(new ParkReviewData(usernamelist.get(i), commentlist.get(i), pngpathlist.get(i)));
                    }

                    ParkReviewAdapter reviewAdapter = new ParkReviewAdapter(getApplicationContext(), reviewDatalist);
                    listView.setAdapter(reviewAdapter);

                }
                else{
                    ArrayList<ParkReviewData> reviewDatalist = new ArrayList<ParkReviewData>();
                    reviewDatalist.add(new ParkReviewData("리뷰 작성자가 아직 없습니다", "가장 먼저 리뷰를 달아주세요", null));
                    ParkReviewAdapter reviewAdapter = new ParkReviewAdapter(getApplicationContext(), reviewDatalist);
                    listView.setAdapter(reviewAdapter);
                }
//

            }
        }, 500); // 0.5초후
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_search:
                Intent intent2 = new Intent(getApplicationContext(), Search_ParkActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_walking:
                Intent intent3 = new Intent(getApplicationContext(), WalkingActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.btn_mypage:
                Intent intent4 = new Intent(getApplicationContext(), WalkListActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.btn_road_home:
                Intent intent5 = new Intent(getApplicationContext(), RoadCloudviewHomeActivity.class);
                intent5.putExtra("roadId", roadId);
                startActivity(intent5);
                finish();
                break;
/*            case R.id.btn_road_review:
                Intent intent6 = new Intent(this, RoadCloudviewReviewActivity.class);
                intent6.putExtra("roadId", roadId);
                intent6.putExtra("lat", startLat);
                intent6.putExtra("lng", startLng);
                startActivity(intent6);
                finish();
                break;*/
            case R.id.btn_road_path:
                Intent intent6 = new Intent(getApplicationContext(), RoadCloudviewPathActivity.class);
                intent6.putExtra("roadId", roadId);
                /*intent6.putExtra("lat", startLat);
                intent6.putExtra("lng", startLng);*/
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

    public void getRoadReview(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getRoadReview(id).enqueue(new Callback<ReviewRes>() {
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

}