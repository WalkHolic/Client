package com.example.walkholic;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.walkholic.DataClass.Data.MyTMapMarkerItem;
import com.example.walkholic.DataClass.Data.ParkInfo;
import com.example.walkholic.DataClass.Data.ParkOption;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.ListItem.SearchItem;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_ParkActivity extends AppCompatActivity implements View.OnClickListener, TMapGpsManager.onLocationChangedCallback, TMapView.OnCalloutRightButtonClickCallback {


    private boolean TrackingMode = true;
    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_search_park;
    Button btn_search_walk;
    Button btn_search_shared;
    Button btn_current_location;
    Button btn_set_location;

    ToggleButton btn_fil_football;
    ToggleButton btn_fil_basketball;
    ToggleButton btn_fil_badminton;
    ToggleButton btn_fil_tennis;
    ToggleButton btn_fil_gateball;
    ToggleButton btn_fil_cycle;
    ToggleButton btn_fil_exercise;
    ToggleButton btn_fil_health;
    ToggleButton btn_fil_summerhouse;
    ToggleButton btn_fil_fountain;
    ToggleButton btn_fil_parking;


    TextInputEditText textInputEditText;
    ImageButton imageButton;


    String API_Key = "l7xxaf0e68fd185f445596200b488c1177af";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;
    //T Map Data
    TMapData tmapdata;

    Double mlat;
    Double mlon;

    private ParkRes parkRes; // 이해찬 추가 (onCreate에서 여기에 주변 공원 리스트를 담습니다)
    Handler mHandler = new Handler();

    ParkOption parkOption = new ParkOption();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_park);
        mlat = 37.0;
        mlon = 127.0;

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
        FrameLayout linearLayoutTmap = (FrameLayout)findViewById(R.id.linearLayoutTmap_park);
        linearLayoutTmap.addView(tMapView);

        // Request For GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // GPS using T Map
        tMapGPS = new TMapGpsManager(this);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        tMapGPS.OpenGps();
        // 키워드 검색으로 요청할 때
        Log.d("dlgochan", "키워드 이름: " + getIntent().getStringExtra("itemName"));
        if (getIntent().getStringExtra("itemName") != null) {
            TrackingMode = false;
            String itemName = getIntent().getStringExtra("itemName");
            mlat = getIntent().getDoubleExtra("itemLat", 37.2844252); // default: 아주대 위경도
            mlon = getIntent().getDoubleExtra("itemLng", 127.043568);

            Log.d("dlgochan", "search Item 위도: " + mlat + "경도: " + mlon);

            // 마커 생성
            TMapPoint tMapPointItem = new TMapPoint(mlat, mlon);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker_blue);
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);                 // bitmap를 Marker icon으로 사용
            tMapMarkerItem.setPosition(0.5f, 1.0f);         // Marker img의 position
            tMapMarkerItem.setTMapPoint(tMapPointItem);         // Marker의 위치
            tMapMarkerItem.setName(itemName);              // Marker의 이름
            tMapView.addMarkerItem(itemName, tMapMarkerItem);
            tMapView.setCenterPoint(mlon, mlat);

        } else { // 그냥 GPS
            TrackingMode = true;
            mlat = tMapGPS.getLocation().getLatitude();
            mlon = tMapGPS.getLocation().getLongitude();

            tMapView.setLocationPoint(mlon, mlat);
            tMapView.setCenterPoint(mlon, mlat);
            Log.d("dlgochan", "위도: " + mlat + "경도: " + mlon);


        }


        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);

        btn_search_park = findViewById(R.id.btn_search_park);
        btn_search_walk = findViewById(R.id.btn_search_walk);
        btn_search_shared = findViewById(R.id.btn_search_shared);
        btn_current_location = findViewById(R.id.btn_current_location);

        btn_set_location = findViewById(R.id.btn_set_location);
        imageButton = findViewById(R.id.imageButton);
        textInputEditText = findViewById(R.id.textInputEditText);

        btn_fil_football = findViewById(R.id.btn_fil_football);
        btn_fil_basketball = findViewById(R.id.btn_fil_basketball);
        btn_fil_badminton = findViewById(R.id.btn_fil_badminton);
        btn_fil_tennis = findViewById(R.id.btn_fil_tennis);
        btn_fil_gateball = findViewById(R.id.btn_fil_gateball);
        btn_fil_cycle = findViewById(R.id.btn_fil_cycle);
        btn_fil_exercise = findViewById(R.id.btn_fil_exercise);
        btn_fil_health = findViewById(R.id.btn_fil_health);
        btn_fil_summerhouse = findViewById(R.id.btn_fil_summerhouse);
        btn_fil_fountain = findViewById(R.id.btn_fil_fountain);
        btn_fil_parking = findViewById(R.id.btn_fil_parking);


        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);

        btn_search_park.setOnClickListener(this);
        btn_search_walk.setOnClickListener(this);
        btn_search_shared.setOnClickListener(this);

        btn_set_location.setOnClickListener(this);
        btn_current_location.setOnClickListener(this);
        imageButton.setOnClickListener(this);


        btn_fil_football.setOnClickListener(this);
        btn_fil_basketball.setOnClickListener(this);
        btn_fil_badminton.setOnClickListener(this);
        btn_fil_tennis.setOnClickListener(this);
        btn_fil_gateball.setOnClickListener(this);
        btn_fil_cycle.setOnClickListener(this);
        btn_fil_exercise.setOnClickListener(this);
        btn_fil_health.setOnClickListener(this);
        btn_fil_summerhouse.setOnClickListener(this);
        btn_fil_fountain.setOnClickListener(this);
        btn_fil_parking.setOnClickListener(this);

        btn_fil_football.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set축구(isChecked);
            }
        });
        btn_fil_basketball.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set농구(isChecked);
            }
        });
        btn_fil_badminton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set배드민턴(isChecked);
            }
        });
        btn_fil_tennis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set테니스(isChecked);
            }
        });
        btn_fil_gateball.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set게이트볼(isChecked);
            }
        });
        btn_fil_cycle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set사이클(isChecked);
            }
        });
        btn_fil_exercise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set운동(isChecked);
            }
        });
        btn_fil_health.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set헬스(isChecked);
            }
        });
        btn_fil_summerhouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set정자(isChecked);
            }
        });
        btn_fil_fountain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set분수(isChecked);
            }
        });
        btn_fil_parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked){
                parkOption.set주차(isChecked);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.btn_search:
//                Intent intent2 = new Intent(getApplicationContext(), Search_ParkActivity.class);
//                startActivity(intent2);
//                finish();
//                break;
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
//            case R.id.btn_search_park:
//                Intent intent5 = new Intent(getApplicationContext(), Search_ParkActivity.class);
//                startActivity(intent5);
//                finish();
//                break;
            case R.id.btn_search_walk:
                Intent intent6 = new Intent(getApplicationContext(), Search_WalkActivity.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.btn_search_shared:
                Intent intent7 = new Intent(getApplicationContext(), Search_SharedActivity.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.btn_set_location:
                Log.d("dlgochan", "위도: " + mlat + "경도: " + mlon);
                getParkByFilter(mlat, mlon, parkOption);

                break;
            case R.id.imageButton:
                ArrayList<SearchItem> searchList = new ArrayList<>();
                Log.d("dlgochan", "Click imageButton with " + textInputEditText.getText().toString());

                tmapdata.findAllPOI(textInputEditText.getText().toString(), poiItem -> {
                    if (poiItem == null) return;
                    for (int i = 0; i < poiItem.size(); i++) {
                        TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                        String poiName = item.getPOIName();
                        String poiAddress = item.getPOIAddress().replace("null", "");
                        TMapPoint point = item.getPOIPoint();
                        SearchItem searchItem = new SearchItem(poiName, poiAddress, point.getLatitude(), point.getLongitude());
                        searchList.add(searchItem);
                        Log.d("dlgochan", "search: " + poiName + poiAddress + point.toString());
                    }
                });

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d("dlgochan", "searchList size: " + searchList.size());
                        Intent intent8 = new Intent(getApplicationContext(), TmapSearchListActivity.class);
                        intent8.putExtra("keyword", textInputEditText.getText().toString());
                        intent8.putParcelableArrayListExtra("searchList", searchList);
                        intent8.putExtra("park", true);
                        startActivity(intent8);
                    }
                }, 500);
            case R.id.btn_current_location:
                TrackingMode = true;
                //화면이동 구현
                tMapView.setTrackingMode(true);
                tMapView.setZoomLevel(17);
                Log.d("dlgochan", "새로고침 버튼 클릭!");
                break;
        }
    }

    @Override
    public void onLocationChange(Location location) {

        if (TrackingMode) {
            mlat = location.getLatitude();
            mlon = location.getLongitude();

            Log.e("dlgochan", "위치변경 탐지");
            //원래 2줄만 있던 코드, 좌표 변경 시 좌표 기록을 해보자
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());

        }

    }

    public void getParkByFilter(double lat, double lng, ParkOption option) {
        final String TAG = "dlgochan";

        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getParkByFilter(lat, lng, option).enqueue(new Callback<ParkRes>() {
            @Override
            public void onResponse(Call<ParkRes> call, Response<ParkRes> response) {
                Log.d(TAG, "onResponse Filter: " + option.toString());
                parkRes = response.body();

                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    addMarketMarker(parkRes.getData());

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
                tMapView.setZoomLevel(13);
            }

            @Override
            public void onFailure(Call<ParkRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());

            }
        });
    }

    public void addMarketMarker(List<ParkInfo> marketList) {
        // 여기에 마커들 초기화 하는 구문 짜주세요

        final String TAG = "dlgochan";
        // Marker img -> bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.info);
        try {
            for (int i = 0; i < marketList.size(); i++) {

                String storeName = marketList.get(i).getName();     // 이름
                String type = marketList.get(i).getType();
                String contact = marketList.get(i).getContact();
                String manageAgency = marketList.get(i).getManageAgency();
                String address = marketList.get(i).getAddrNew();         // 주소
                String addr = marketList.get(i).getAddr();
                double lat = marketList.get(i).getLat();            // 위도
                double lon = marketList.get(i).getLng();           // 경도
                String ParkId = marketList.get(i).getParkId();
                int ID = marketList.get(i).getId();


                // TMapPoint
                TMapPoint tMapPoint = new TMapPoint(lat, lon);

                // TMapMarkerItem
                // Marker Initial Settings
                MyTMapMarkerItem tMapMarkerItem = new MyTMapMarkerItem();


                //공원정보 값 전달
                tMapMarkerItem.setParkId(ParkId);
                tMapMarkerItem.setId(ID);


                // TMapMarkerItem
                // Marker Initial Settings


                tMapMarkerItem.setIcon(bitmap);                 // bitmap를 Marker icon으로 사용
                tMapMarkerItem.setPosition(0.5f, 1.0f);         // Marker img의 position
                tMapMarkerItem.setTMapPoint(tMapPoint);         // Marker의 위치
                tMapMarkerItem.setName(storeName);              // Marker의 이름

                // Balloon View Initial Settings
                tMapMarkerItem.setCanShowCallout(true);         // Balloon View 사용
                tMapMarkerItem.setCalloutTitle(storeName);      // Main Message
                tMapMarkerItem.setCalloutSubTitle(address);     // Sub Message
                tMapMarkerItem.setAutoCalloutVisible(false);    // 초기 접속 시 Balloon View X
                tMapMarkerItem.setCalloutRightButtonImage(bitmap2); //구름뷰 오른쪽 비트맨 클릭시 onCalloutRightButton호출


                // add Marker on T Map View
                // id로 Marker을 식별
                tMapView.addMarkerItem("marketLocation" + i, tMapMarkerItem);


            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "onFailure : 널");
        }


    }


    @Override
    public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
        final String TAG = "dlgochan";
        // Toast.makeText(this, "풍선뷰 클릭", Toast.LENGTH_SHORT).show();


        MyTMapMarkerItem parkInfo = (MyTMapMarkerItem) tMapMarkerItem;
        Log.d(TAG, "풍선뷰 파크아이디:" + parkInfo.getId());
        int Id = parkInfo.getId();

        Intent intent = new Intent(getApplicationContext(), ParkCloudviewHomeActivity.class);
        intent.putExtra("ID", Id);
        intent.putExtra("lat", mlat);
        intent.putExtra("lng", mlon);
        startActivity(intent);


    }
}