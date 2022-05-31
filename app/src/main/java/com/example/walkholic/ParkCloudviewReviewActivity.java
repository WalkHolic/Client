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
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkCloudviewReviewActivity extends AppCompatActivity implements View.OnClickListener {


    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_review;
    Button btn_park_home;

    ListView listView;

    ImageView parkimageview;
    String png_path;

    int ParkId_int;

    boolean path_check = false;

    private ParkRes parkRes;
    private ReviewRes reviewRes;

    Handler mHandler = new Handler();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_cloudview_review);

        parkimageview = findViewById(R.id.parkimageView);

        // Log.d(TAG, "풍선뷰 정보 :  " + parkInfo.getPngPath() );
        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);
        btn_review = findViewById(R.id.btn_park_review);
        btn_park_home = findViewById(R.id.btn_park_home);

        listView = findViewById(R.id.park_review_list);
        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_review.setOnClickListener(this);
        btn_park_home.setOnClickListener(this);

        ArrayList<String> usernamelist = new ArrayList<>();
        ArrayList<String> commentlist = new ArrayList<>();
        ArrayList<String> pngpathlist = new ArrayList<>();
        Intent intent = getIntent();
        ParkId_int = intent.getIntExtra("ID", ParkId_int);
        getParkById(ParkId_int);
        getParkReview(ParkId_int);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                png_path = parkRes.getData().get(0).getPngPath();

                if (png_path == null) {
                    path_check = true;
                }
                if (path_check) {
                    parkimageview.setImageResource(R.drawable.basic_park);

                } else {
                    Glide.with(getApplicationContext()).load(png_path).into(parkimageview);
                }

//                Log.d(TAG, "널 아님!");
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
            case R.id.btn_park_home:
                Intent intent5 = new Intent(getApplicationContext(), ParkCloudviewHomeActivity.class);
                int Id = ParkId_int;
                intent5.putExtra("ID", Id);
                startActivity(intent5);
                break;
        }
    }

//    public ArrayList<ReviewData> InitializeReview() {
//        reviewDatalist = new ArrayList<ReviewData>();
//
//        for (int i = 0; i < reviewRes.getData().size(); i++) {
//            reviewDatalist.add(new ReviewData(usernamelist.get(i), commentlist.get(i), pngpathlist.get(i)));
//
//        }
//        return reviewDatalist;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getParkById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getParkById(id).enqueue(new Callback<ParkRes>() {
            @Override
            public void onResponse(Call<ParkRes> call, Response<ParkRes> response) {
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

    public void getParkReview(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getParkReview(id).enqueue(new Callback<ReviewRes>() {
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