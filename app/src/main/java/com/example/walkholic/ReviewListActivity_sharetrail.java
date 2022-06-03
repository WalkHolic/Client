package com.example.walkholic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Data.Review;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewListActivity_sharetrail extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_mypage2;
    Button btn_modify,btn_delete;

    ConstraintLayout park, trail, shareTrail;
    Handler handler = new Handler();
    ReviewListViewAdapter adapter;
    Review temp;
    ImageView imageView;
    Uri imageUri;
    ListView reviewListView;
    UserRoadRes userRoad;

    private ReviewRes reviewRes;
    private ReviewRequestDto reviewRequestDto;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_share_trail);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_mypage2 = findViewById(R.id.btn_myList2);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();
        editor.putInt("objectType", 3);
        editor.commit();

        park = findViewById(R.id.parkReview);
        trail = findViewById(R.id.trailReview);
        shareTrail = findViewById(R.id.shareTrailReview);
        reviewListView = (ListView) findViewById(R.id.sharedTrailReviewListView);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_mypage2.setOnClickListener(this);


        park.setOnClickListener(this);
        trail.setOnClickListener(this);
        shareTrail.setOnClickListener(this);

        imageView = findViewById(R.id.user_image);

        getMySharedTrailReview();

        handler.postDelayed(new Runnable() {
            public void run() {
                // 시간 지난 후 실행할 코딩
                displayList();
            }
        }, 800); // 0.5초후

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
            case R.id.btn_myList2:
                Intent intent5 = new Intent(getApplicationContext(), WalkListActivity.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.trailReview:
                Intent intent6 = new Intent(getApplicationContext(), ReviewListActivity_trail.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.shareTrailReview:
                Intent intent7 = new Intent(getApplicationContext(), ReviewListActivity_sharetrail.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.parkReview:
                Intent intent8 = new Intent(getApplicationContext(), ReviewListActivity_park.class);
                startActivity(intent8);
                finish();
                break;
        }
    }

    public void displayList() {

        adapter = new ReviewListViewAdapter(this);
        Log.d("리스트뷰테스트", "테테테테테스트");
        if (reviewRes.getData() == null) return;
        for (int i = 0; i < reviewRes.getData().size(); i++) {
            temp = reviewRes.getData().get(i);
            getUserRoadById(temp.getParkId());
            // 0: 리뷰내용 1: 리뷰별점 2: 리뷰사진
            Log.d("리스트뷰테스트", "mmmm : "+temp.getContent()+" , "+temp.getScore()+ " , "+temp.getPngPath()+" , "+temp.getId()+" , "+temp.getParkId());
            adapter.addItemToList(temp.getContent(), temp.getScore(), temp.getPngPath(),temp.getId(),temp.getParkId());

        }
        reviewListView.setAdapter(adapter);
        Log.d("리스트뷰테스트", "테테테테테스트2");
    }

    public void getUserRoadById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getUserRoadById(id).enqueue(new Callback<UserRoadRes>() {
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) {
                if (response.isSuccessful()) {
                    userRoad = response.body();
                    Log.d(TAG, "onResponse Success : " + userRoad.toString());
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
    public void getMySharedTrailReview() {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getMySharedTrailReview().enqueue(new Callback<ReviewRes>() {
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