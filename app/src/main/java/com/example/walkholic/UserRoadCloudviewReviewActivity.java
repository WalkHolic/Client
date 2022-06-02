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
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRoadCloudviewReviewActivity extends AppCompatActivity implements View.OnClickListener {


    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_user_road_home;
    Button btn_user_road_review;
    Button btn_user_road_path;

    ImageView userRoadImageView;

    ListView listView;

    ImageView roadimageview;

    String picturePath;
    Double startLat;
    Double startLng;

    int userRoadId;

    private UserRoadRes userRoadRes;
    private ReviewRes reviewRes;

    Handler mHandler = new Handler();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_road_cloudview_review);

        Intent intent = getIntent();
        userRoadId = intent.getIntExtra("userRoadId", userRoadId);
        double mlat = intent.getDoubleExtra("lat", 37.2844252); // default: 아주대 위경도
        double mlon = intent.getDoubleExtra("lng", 127.043568);
        Log.d(TAG, "받아온 intent 값: " + userRoadId + "/" + mlat + "/" + mlon);

        roadimageview = (ImageView) findViewById(R.id.roadimageView);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);

        btn_user_road_home = findViewById(R.id.btn_user_road_home);
        btn_user_road_review = findViewById(R.id.btn_user_road_review);
        btn_user_road_path = findViewById(R.id.btn_user_road_path);
        userRoadImageView = (ImageView) findViewById(R.id.userRoadImageView);

        listView = findViewById(R.id.user_road_review_list);


        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_user_road_home.setOnClickListener(this);
        btn_user_road_review.setOnClickListener(this);
        btn_user_road_path.setOnClickListener(this);

        ArrayList<String> usernamelist = new ArrayList<>();
        ArrayList<String> commentlist = new ArrayList<>();
        ArrayList<String> pngpathlist = new ArrayList<>();

        getUserRoadById(userRoadId);
        getUserRoadReview(userRoadId);


        mHandler.postDelayed(new Runnable() {
            public void run() {
                picturePath = userRoadRes.getData().get(0).getPicture();

                if (picturePath != null) {
                    Glide.with(getApplicationContext()).load(picturePath).into(userRoadImageView);
                }else{
                    userRoadImageView.setImageResource(R.drawable.basic_park);
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
            case R.id.btn_user_road_home:
                Intent intent5 = new Intent(this, UserRoadCloudviewHomeActivity.class);
                intent5.putExtra("userRoadId", userRoadId);
                startActivity(intent5);
                break;
/*            case R.id.btn_road_review:
                Intent intent6 = new Intent(this, RoadCloudviewReviewActivity.class);
                intent6.putExtra("roadId", roadId);
                intent6.putExtra("lat", startLat);
                intent6.putExtra("lng", startLng);
                startActivity(intent6);
                finish();
                break;*/
            case R.id.btn_user_road_path:
                Intent intent6 = new Intent(this, UserRoadCloudviewPathActivity.class);
                intent6.putExtra("userRoadId", userRoadId);
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

    public void getUserRoadReview(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getUserRoadReview(id).enqueue(new Callback<ReviewRes>() {
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