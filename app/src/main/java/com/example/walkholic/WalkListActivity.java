package com.example.walkholic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Data.UserRoad;
import com.example.walkholic.DataClass.Data.UserRoadPath;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.RoadPathRes;
import com.example.walkholic.DataClass.Response.RoadRes;
import com.example.walkholic.DataClass.Response.UserRoadPathRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.DataClass.Response.UserRoadSharedRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalkListActivity extends AppCompatActivity implements View.OnClickListener {

    private ParkRes parkRes;
    private UserRoadRes userRoadRes;
    private RoadRes roadRes;
    private ReviewRes reviewRes;
    private RoadPathRes roadPathRes;
    private UserRoadPathRes userRoadPathRes;
    private UserRoadSharedRes userRoadSharedRes;

    private String address1 = "";
    UserRoad temp = new UserRoad();

    //T Map Data
    TMapData tmapdata;

    List<UserRoad> userRoadList;
    List<UserRoadPath> userRoadPathList;
    Handler handler = new Handler();

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_review;

    ListView trailListView;
    TrailListViewAdapter adapter;

    ImageView imageView;
    Uri imageUri;
    private ReviewRequestDto reviewRequestDto;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_list);

        //T Map Data
        tmapdata = new TMapData();

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_review = findViewById(R.id.btn_review);

        trailListView = (ListView) findViewById(R.id.trailListView);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_review.setOnClickListener(this);


        imageView = findViewById(R.id.user_image);

        load();
        handler.postDelayed(new Runnable() {
            public void run() {
                // 시간 지난 후 실행할 코딩
                displayList();
            }
        }, 800); // 0.5초후


    }


    @WorkerThread
    public void load() {
        getMyRoad();
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
            case R.id.btn_review:
                Intent intent5 = new Intent(getApplicationContext(), ReviewListActivity_park.class);
                startActivity(intent5);
                finish();
                break;

        }
    }
  
    public void getUserRoadPathById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getUserRoadPathById(id).enqueue(new Callback<UserRoadPathRes>() {

            @Override
            public void onResponse(Call<UserRoadPathRes> call, Response<UserRoadPathRes> response) {
                if (response.isSuccessful()) {
                    userRoadPathRes = response.body();
                    Log.d(TAG, "onResponse Success : " + userRoadPathRes.toString());
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
            public void onFailure(Call<RoadPathRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public void getMyRoad() {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getMyRoad().enqueue(new Callback<UserRoadRes>() {
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) {
                if (response.isSuccessful()) {
                    userRoadRes = response.body();
                    userRoadList = userRoadRes.getData();
                    Log.d(TAG, "onResponse Success, userRoadList.size() : " + userRoadList.size() + " ,   hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

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

    public void displayList() {

        adapter = new TrailListViewAdapter(this);

        if (userRoadList == null) return;
        for (int i = 0; i < userRoadList.size(); i++) {
            temp = userRoadList.get(i);
            // 0: 이미지, 1 : 산책로명 2 : 해시태그들  3 : 산책로 길이  4 : 산책로시작지점 5 : 산책로 끝지점 6 : 산책로 설명
            adapter.addItemToList(temp.getPicture(), temp.getTrailName(), temp.getHashtag(), temp.getDistance(), temp.getStartAddr(), address1, temp.getDescription(), temp.getId(),temp.getShared());
        }
        trailListView.setAdapter(adapter);

    }


}