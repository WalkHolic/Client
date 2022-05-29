package com.example.walkholic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkCloudviewFacilityActivity extends AppCompatActivity implements View.OnClickListener{


    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_park_review, btn_park_info;


    ImageView parkimageview;
    String sport;
    String amuse;
    String conv;
    String etc;
    String png_path;

    int ParkId_int;

    TextView facility_sport;
    TextView facility_amuse;
    TextView facility_conv;
    TextView facility_etc;
    private ParkRes parkRes;

    Handler mHandler = new Handler();


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_cloudview_home);

        Intent intent = getIntent();
        ParkId_int = intent.getIntExtra("ID",ParkId_int);

        getParkById(ParkId_int);

        mHandler.postDelayed(new Runnable()  {
            public void run() {
                sport = parkRes.getData().get(0).getFacilitySport();
                amuse = parkRes.getData().get(0).getFacilityAmuse();
                conv = parkRes.getData().get(0).getFacilityConv();
                etc = parkRes.getData().get(0).getFacilityEtc();
                etc += ", " + parkRes.getData().get(0).getFacilityCul();

                Glide.with(getApplicationContext()).load(png_path).into(parkimageview);


                facility_sport.setText(sport);
                facility_amuse.setText(amuse);
                facility_conv.setText(conv);
                facility_etc.setText(etc);
            }
        }, 500); // 0.5초후


        parkimageview = findViewById(R.id.parkimageView);

        Glide.with(this).load(png_path).into(parkimageview);

        btn_home =  findViewById(R.id.btn_home);
        btn_search =  findViewById(R.id.btn_search);
        btn_walking =  findViewById(R.id.btn_walking);
        btn_mypage =  findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);
        btn_park_review = findViewById(R.id.btn_park_review);
        btn_park_info = findViewById(R.id.btn_park_info);




        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_park_review.setOnClickListener(this);
        btn_park_info.setOnClickListener(this);
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
            case R.id.btn_park_review:
                Intent intent5 = new Intent(getApplicationContext(), ParkCloudviewReviewActivity.class);
                int Id = ParkId_int;
                intent5.putExtra("ID", Id);
                startActivity(intent5);
                break;
            case R.id.btn_park_info:
                Intent intent6 = new Intent(getApplicationContext(), ParkCloudviewHomeActivity.class);
                int Id1 = ParkId_int;
                intent6.putExtra("ID", Id1);
                startActivity(intent6);
                break;

        }
    }


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
}