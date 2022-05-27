//package com.example.walkholic;
//
//import android.Manifest;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.walkholic.DataClass.Data.ParkInfo;
//import com.example.walkholic.DataClass.Data.ParkOption;
//import com.example.walkholic.DataClass.Response.ParkRes;
//import com.example.walkholic.DataClass.Response.UserRoadRes;
//import com.example.walkholic.Service.ServerRequestApi;
//import com.example.walkholic.Service.ServiceGenerator;
//import com.skt.Tmap.TMapData;
//import com.skt.Tmap.TMapGpsManager;
//import com.skt.Tmap.TMapMarkerItem;
//import com.skt.Tmap.TMapPoint;
//import com.skt.Tmap.TMapView;
//
//import java.io.IOException;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class WriteReviewActivity extends AppCompatActivity implements View.OnClickListener {
//
//    Button btn_home;
//    Button btn_search;
//    Button btn_walking;
//    Button btn_mypage;
//
//    Button btn_back;
//
//    Button btn_info;
//    Button btn_review;
//    Button btn_facility;
//
//    private ParkInfo parkInfo; // 이해찬 추가 (onCreate에서 여기에 공원 정보를 담습니다)
//    private UserRoadRes userRoadRes;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_park);
//
//
//        btn_home =  findViewById(R.id.btn_home);
//        btn_search =  findViewById(R.id.btn_search);
//        btn_walking =  findViewById(R.id.btn_walking);
//        btn_mypage =  findViewById(R.id.btn_mypage);
//
//        btn_info.findViewById(R.id.btn_info);
//        btn_review.findViewById(R.id.btn_review);
//        btn_facility.findViewById(R.id.btn_facility);
//
//        btn_back.findViewById(R.id.btn_back);
//
//        btn_home.setOnClickListener(this);
//        btn_search.setOnClickListener(this);
//        btn_walking.setOnClickListener(this);
//        btn_mypage.setOnClickListener(this);
//
//        btn_info.setOnClickListener(this);
//        btn_review.setOnClickListener(this);
//        btn_facility.setOnClickListener(this);
//
//        btn_back.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_home:
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.btn_search:
//                Intent intent2 = new Intent(getApplicationContext(), Search_ParkActivity.class);
//                startActivity(intent2);
//                finish();
//                break;
//            case R.id.btn_walking:
//                Intent intent3 = new Intent(getApplicationContext(), WalkingActivity.class);
//                startActivity(intent3);
//                finish();
//                break;
//            case R.id.btn_mypage:
//                Intent intent4 = new Intent(getApplicationContext(), WalkListActivity.class);
//                startActivity(intent4);
//                finish();
//                break;
//            case R.id.btn_info:
//                Intent intent5 = new Intent(getApplicationContext(), Search_ParkActivity.class);
//                startActivity(intent5);
//                finish();
//                break;
//            case R.id.btn_review:
//                Intent intent6 = new Intent(getApplicationContext(), Search_WalkActivity.class);
//                startActivity(intent6);
//                finish();
//                break;
//            case R.id.btn_facility:
//                Intent intent7 = new Intent(getApplicationContext(), Search_SharedActivity.class);
//                startActivity(intent7);
//                finish();
//                break;
//            case R.id.back:
//                //뒤로가기
//                break;
//        }
//    }
