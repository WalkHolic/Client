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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

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
    Button btn_park_home;
    Button btn_park_facility;
    Button btn_write_review;

    ProgressBar progressBar5;
    ProgressBar progressBar4;
    ProgressBar progressBar3;
    ProgressBar progressBar2;
    ProgressBar progressBar1;

    TextView txt_review_mean;
    TextView txt_nr_five_star;
    TextView txt_nr_four_star;
    TextView txt_nr_three_star;
    TextView txt_nr_two_star;
    TextView txt_nr_one_star;

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
        btn_park_home = findViewById(R.id.btn_park_home);
        btn_park_facility = findViewById(R.id.btn_park_facility);
        btn_write_review = findViewById(R.id.btn_write_review);

        progressBar5 = findViewById(R.id.progressBar5);
        progressBar4 = findViewById(R.id.progressBar4);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar1 = findViewById(R.id.progressBar1);
        txt_review_mean = findViewById(R.id.txt_review_mean);

        txt_nr_five_star = findViewById(R.id.txt_nr_five_star);
        txt_nr_four_star = findViewById(R.id.txt_nr_four_star);
        txt_nr_three_star = findViewById(R.id.txt_nr_three_star);
        txt_nr_two_star = findViewById(R.id.txt_nr_two_star);
        txt_nr_one_star = findViewById(R.id.txt_nr_one_star);

        listView = findViewById(R.id.park_review_list);
        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_park_home.setOnClickListener(this);
        btn_park_facility.setOnClickListener(this);
        btn_write_review.setOnClickListener(this);

        ArrayList<String> usernamelist = new ArrayList<>();
        ArrayList<Double> scoreList = new ArrayList<>();
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
                if(reviewRes != null){
                    Log.d(TAG, "널 아님!");
                    for (int i = 0; i < reviewRes.getData().size(); i++) {
                        String username = reviewRes.getData().get(i).getUserName();
                        double score = reviewRes.getData().get(i).getScore();
                        String comment = reviewRes.getData().get(i).getContent();
                        String pngpath = reviewRes.getData().get(i).getPngPath();

                        Log.d(TAG, "유저이름 : " + username);
                        Log.d(TAG, "레이팅 : " + score);
                        Log.d(TAG, "리뷰 : " + comment);
                        Log.d(TAG, "사진경로 : " + pngpath);

                        usernamelist.add(username);
                        scoreList.add(score);
                        commentlist.add(comment);
                        pngpathlist.add(pngpath);
                    }
                    int starList[] = new int[] {0,0,0,0,0};
                    // scoreList 통해서 별점 평균 계산하기
                    for (Double score : scoreList) {
                        starList[score.intValue()-1]++;
                    }
                    Log.d(TAG, "별점 리스트: " + Arrays.toString(starList));
                    OptionalDouble average = scoreList.stream().mapToDouble(s -> s).average();
                    if (average.isPresent()) {
                        double reviewStarAverage = average.getAsDouble();
                        txt_review_mean.setText(String.format("%,.1f", reviewStarAverage) + "점");
                    } else {
                        txt_review_mean.setText("0점");
                    }
                    progressBar5.setMax(scoreList.size());
                    progressBar4.setMax(scoreList.size());
                    progressBar3.setMax(scoreList.size());
                    progressBar2.setMax(scoreList.size());
                    progressBar1.setMax(scoreList.size());
                    progressBar5.setProgress(starList[4]);
                    progressBar4.setProgress(starList[3]);
                    progressBar3.setProgress(starList[2]);
                    progressBar2.setProgress(starList[1]);
                    progressBar1.setProgress(starList[0]);
                    txt_nr_five_star.setText(String.valueOf(starList[4]));
                    txt_nr_four_star.setText(String.valueOf(starList[3]));
                    txt_nr_three_star.setText(String.valueOf(starList[2]));
                    txt_nr_two_star.setText(String.valueOf(starList[1]));
                    txt_nr_one_star.setText(String.valueOf(starList[0]));

                    Log.d(TAG, usernamelist.toString());
                    ArrayList<ParkReviewData> reviewDatalist = new ArrayList<ParkReviewData>();
                    for (int i = 0; i < usernamelist.size(); i++) {
                        reviewDatalist.add(new ParkReviewData(usernamelist.get(i), scoreList.get(i), commentlist.get(i), pngpathlist.get(i)));
                    }

                    ParkReviewAdapter reviewAdapter = new ParkReviewAdapter(getApplicationContext(), reviewDatalist);
                    listView.setAdapter(reviewAdapter);

                }
                else{
                    ArrayList<ParkReviewData> reviewDatalist = new ArrayList<ParkReviewData>();
                    reviewDatalist.add(new ParkReviewData("리뷰 작성자가 아직 없습니다", 0,"가장 먼저 리뷰를 달아주세요", null));
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
            case R.id.btn_park_home:
                Intent intent5 = new Intent(getApplicationContext(), ParkCloudviewHomeActivity.class);
                intent5.putExtra("ID", ParkId_int);
                startActivity(intent5);
                finish();
                break;
            case R.id.btn_park_facility:
                Intent intent6 = new Intent(getApplicationContext(), ParkCloudviewFacilityActivity.class);
                intent6.putExtra("ID", ParkId_int);
                startActivity(intent6);
                finish();
                break;
            case R.id.btn_write_review:
                Intent intent7 = new Intent(getApplicationContext(), WriteReviewActivity.class);
                intent7.putExtra("ID", ParkId_int);
                intent7.putExtra("name", parkRes.getData().get(0).getName());
                startActivity(intent7);
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