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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

    Button btn_fil_football;
    Button btn_fil_basketball;
    Button btn_fil_badminton;
    Button btn_fil_tennis;
    Button btn_fil_gateball;
    Button btn_fil_cycle;
    Button btn_fil_exercise;
    Button btn_fil_health;
    Button btn_fil_summerhouse;
    Button btn_fil_fountain;
    Button btn_fil_parking;


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
                //getParkByCurrentLocation(37.3015045429, 127.0312636113);
                Log.d("dlgochan", "위도: " + mlat + "경도: " + mlon);
                getParkByCurrentLocation(mlat, mlon);

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

            //김재현 추가 해찬님 검토바랍니다
            case R.id.btn_fil_football:
                setParkOption("축구");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_basketball:
                setParkOption("농구");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_badminton:
                setParkOption("배드민턴");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_tennis:
                setParkOption("테니스");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_gateball:
                setParkOption("게이트볼");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_cycle:
                setParkOption("사이클");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_exercise:
                setParkOption("운동");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_health:
                setParkOption("헬스");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_summerhouse:
                setParkOption("정자");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_fountain:
                setParkOption("분수");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
                break;
            case R.id.btn_fil_parking:
                setParkOption("주차");
                Log.d("dlgochan", " 파크옵션정보 : " +parkOption.toString());
                getParkByFilter(mlat,mlon, parkOption);
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

    public void getParkByCurrentLocation(double lat, double lng) {
        //이해찬 추가
        /////////////////////////////////////////////////////////////////////////
        final String TAG = "dlgochan";
        // 안드로이드 앱 내부 파일 (SharedPreference) 에서 jwt 값 가져오기
//        context = this;
//        String token = PreferenceManager.getString(context, "token");
//        Log.d(TAG, "onCreate Token: " + token);
        //서비스 생성 (항상 헤더에 토큰을 담아서 리퀘스트)
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        // 알맞는 request 형식 (여기서는 token) 을 파라미터로 담아서 리퀘스트
//        service.getParkByCurrentLocation(currentLat, currentLng).enqueue(new Callback<ParkList>() {
        service.getParkByCurrentLocation(lat, lng).enqueue(new Callback<ParkRes>() { // ( 여기 숫자부분에 GPS 정보 받아와서 넣어주시면 정상 작동할 것 같습니다 )
            @Override
            public void onResponse(Call<ParkRes> call, Response<ParkRes> response) { // Call<타입> : 타입을 잘 맞춰주시면 됩니다. ex) 산책로 조회는 RoadList, 산책로 경로 조회는 RoadPath
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    parkRes = response.body();
                    Log.d(TAG, "onResponse Success : " + parkRes.toString());
                    addMarketMarker(parkRes.getData());
                    tMapView.setZoomLevel(13);

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

    //김재현 추가 해찬님 검토바랍니다
    public void setParkOption(String option){
        switch (option) {
            case "축구":
                parkOption.set축구(true);
                break;
            case "농구":
                parkOption.set농구(true);
                break;
            case "배드민턴":
                parkOption.set배드민턴(true);
                break;
            case "테니스":
                parkOption.set테니스(true);
                break;
            case "게이트볼":
                parkOption.set게이트볼(true);
                break;
            case "사이클":
                parkOption.set사이클(true);
                break;
            case "운동":
                parkOption.set운동(true);
                break;
            case "헬스":
                parkOption.set헬스(true);
                break;
            case "정자":
                parkOption.set정자(true);
                break;
            case "분수":
                parkOption.set분수(true);
                break;
            case "주차":
                parkOption.set주차(true);
                break;


        }
    }



    public void getParkByFilter(double lat, double lng, ParkOption option) {
        final String TAG = "dlgochan";

        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getParkByFilter(lat, lng, option).enqueue(new Callback<ParkRes>() {
            @Override
            public void onResponse(Call<ParkRes> call, Response<ParkRes> response) {
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    parkRes = response.body();
                    addMarketMarker(parkRes.getData());
                    tMapView.setZoomLevel(13);
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

    public void addMarketMarker(List<ParkInfo> marketList) {
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