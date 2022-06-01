package com.example.walkholic;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Data.RoadPath;
import com.example.walkholic.DataClass.Data.UserRoadPath;
import com.example.walkholic.DataClass.Response.UserRoadPathRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRoadCloudviewPathActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "dlgochan";
    String API_Key = "l7xxaf0e68fd185f445596200b488c1177af";

    // T Map View
    TMapView tMapView = null;

    //T Map Data
    TMapData tmapdata;

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;
    Button btn_user_road_home;

    ImageView userRoadImageView;
    String picturePath;

    int userRoadId;

    private UserRoadRes userRoadRes;
    private UserRoadPathRes userRoadPathRes;
    List<UserRoadPath> roadPathList;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_road_cloudview_path);

        //T Map Data
        tmapdata = new TMapData();

        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        // Initial Setting
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = findViewById(R.id.linearLayoutTmap_userRoad);
        linearLayoutTmap.addView(tMapView);

        // Request For GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Intent intent = getIntent();
        userRoadId = intent.getIntExtra("userRoadId", userRoadId);
        double mlat = intent.getDoubleExtra("lat", 37.2844252); // default: 아주대 위경도
        double mlon = intent.getDoubleExtra("lng", 127.043568);
        Log.d(TAG, "받아온 intent 값: " + userRoadId + "/" + mlat + "/" + mlon);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);

        btn_user_road_home = findViewById(R.id.btn_user_road_home);
        userRoadImageView = (ImageView) findViewById(R.id.userRoadImageView);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_user_road_home.setOnClickListener(this);

        getUserRoadById(userRoadId);
        getUserRoadPathById(userRoadId);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                picturePath = userRoadRes.getData().get(0).getPicture();
                roadPathList = userRoadPathRes.getData();
                Log.d(TAG, "산책로 path: " + roadPathList);

                tMapView.setCenterPoint(mlon, mlat);
                tMapView.setZoomLevel(14);


                List<Double> latList = roadPathList.stream().map(roadPath -> roadPath.getLat()).collect(Collectors.toList());
                List<Double> lngList = roadPathList.stream().map(roadPath -> roadPath.getLng()).collect(Collectors.toList());
                try {
                    drawTrail2(latList, lngList);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }

                if (picturePath != null) {
                    Glide.with(getApplicationContext()).load(picturePath).into(userRoadImageView);
                }
            }
        }, 300); // 0.3초후

    }

    public void drawTrail2(List<Double> latList, List<Double> lngList) throws IOException, ParserConfigurationException, SAXException {
        new Thread(() -> {
            ArrayList<TMapPoint> tempList = new ArrayList<>();
            for (int i = 0; i < latList.size(); i++) {
                Double tmpLat = latList.get(i);
                Double tmpLng = lngList.get(i);
                if (tmpLat == null || tmpLng == null) continue;
                TMapPoint tMapPoint = new TMapPoint(latList.get(i), lngList.get(i));
                tempList.add(tMapPoint);
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker_yellow);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.marker_blue);

            TMapPoint tMapPointStart;
            TMapPoint tMapPointEnd;
            Log.d(TAG, "drawTrail2");
            for (int i = 0; i < tempList.size() - 1; i++) {
                tMapPointStart = tempList.get(i);
                tMapPointEnd = tempList.get(i + 1);
                if (i == 0) {
                    TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                    tMapMarkerItem.setIcon(bitmap);
                    tMapMarkerItem.setPosition(0.5f, 1.0f);
                    tMapMarkerItem.setTMapPoint(tMapPointStart);
                    tMapMarkerItem.setName("출발");
                    tMapMarkerItem.setCalloutTitle("출발");      // Main Message
                    tMapMarkerItem.setCanShowCallout(true);         // Balloon View 사용
                    tMapView.addMarkerItem("start", tMapMarkerItem);
                } else if (i == tempList.size() - 2) {
                    TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                    tMapMarkerItem.setIcon(bitmap2);
                    tMapMarkerItem.setPosition(0.5f, 1.0f);
                    tMapMarkerItem.setTMapPoint(tMapPointEnd);
                    tMapMarkerItem.setName("도착");
                    tMapMarkerItem.setCalloutTitle("도착");      // Main Message
                    tMapMarkerItem.setCanShowCallout(true);         // Balloon View 사용
                    tMapView.addMarkerItem("finish", tMapMarkerItem);
                }

                TMapPolyLine tMapPolyLine = null;
                try {
                    tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                tMapPolyLine.setLineColor(Color.RED);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("Line" + i, tMapPolyLine);
                tMapView.setCenterPoint(tMapPointStart.getLongitude(), tMapPointStart.getLatitude());
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    Log.d(TAG, "경로 보여주기 예외: " + e.getMessage());
                }
            }
        }).start();
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
            case R.id.btn_user_road_home:
                Intent intent5 = new Intent(this, UserRoadCloudviewHomeActivity.class);
                intent5.putExtra("userRoadId", userRoadId);
                startActivity(intent5);
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
            public void onFailure(Call<UserRoadPathRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
}