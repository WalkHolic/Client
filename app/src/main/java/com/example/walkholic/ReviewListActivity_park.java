package com.example.walkholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewListActivity_park extends AppCompatActivity implements View.OnClickListener {

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_mypage2;
    ConstraintLayout park, trail, shareTrail;

    ImageView imageView;
    Uri imageUri;

    private ReviewRes reviewRes;
    private ReviewRequestDto reviewRequestDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list_park);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_mypage2 = findViewById(R.id.btn_myList2);


        trail = findViewById(R.id.trailReview);
        shareTrail = findViewById(R.id.shareTrailReview);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_mypage2.setOnClickListener(this);

        trail.setOnClickListener(this);
        shareTrail.setOnClickListener(this);

        imageView = findViewById(R.id.user_image);

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

        }
    }


}