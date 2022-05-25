package com.example.walkholic;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.walkholic.DTO.ParkInfo;
import com.example.walkholic.DTO.ParkRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_ParkActivity extends AppCompatActivity implements View.OnClickListener,  TMapGpsManager.onLocationChangedCallback, TMapView.OnCalloutRightButtonClickCallback {

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_search_park;
    Button btn_search_walk;
    Button btn_search_shared;

    Button btn_set_location;

    String API_Key = "l7xxaf0e68fd185f445596200b488c1177af";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;
    //T Map Data
    TMapData tmapdata;

    Double mlat;
    Double mlon;

//    private Context context; // 이해찬 추가
    private ServerRequestApi service; // 이해찬 추가
    private ParkRes parkRes; // 이해찬 추가 (onCreate에서 여기에 주변 공원 리스트를 담습니다)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_park);


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
        LinearLayout linearLayoutTmap = findViewById(R.id.linearLayoutTmap_park);
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

        mlat = tMapGPS.getLocation().getLatitude();
        mlon = tMapGPS.getLocation().getLongitude();
        Log.d("dlgochan", "위도: " + mlat + "경도: " + mlon);


        tMapView.setLocationPoint(mlon, mlat);
        tMapView.setCenterPoint(mlon, mlat);

        btn_home =  findViewById(R.id.btn_home);
        btn_search =  findViewById(R.id.btn_search);
        btn_walking =  findViewById(R.id.btn_walking);
        btn_mypage =  findViewById(R.id.btn_mypage);

        btn_search_park = findViewById(R.id.btn_search_park);
        btn_search_walk = findViewById(R.id.btn_search_walk);
        btn_search_shared = findViewById(R.id.btn_search_shared);

        btn_set_location = findViewById(R.id.btn_set_location);


        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);

        btn_search_park.setOnClickListener(this);
        btn_search_walk.setOnClickListener(this);
        btn_search_shared.setOnClickListener(this);

        btn_set_location.setOnClickListener(this);

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
            case R.id.btn_search_park:
                Intent intent5 = new Intent(getApplicationContext(), Search_ParkActivity.class);
                startActivity(intent5);
                finish();
                break;
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
                setlocation();
                break;

        }
    }

    @Override
    public void onLocationChange(Location location) {

        mlat = location.getLatitude();
        mlon = location.getLongitude();

        Log.e("asdasd", "위치변경");
        //원래 2줄만 있던 코드, 좌표 변경 시 좌표 기록을 해보자
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        /*TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();

        Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.marker_blue);
        tMapMarkerItem.setIcon(markerIcon); // 마커 아이콘 지정

        tMapMarkerItem.setTMapPoint(new TMapPoint(location.getLongitude(), location.getLatitude()));
        tMapMarkerItem.setName("marker");
        tMapView.addMarkerItem("marker", tMapMarkerItem);*/

    }

    public void setlocation(){
            //이해찬 추가
            /////////////////////////////////////////////////////////////////////////
            final String TAG = "dlgochan";
            // 안드로이드 앱 내부 파일 (SharedPreference) 에서 jwt 값 가져오기
//        context = this;
//        String token = PreferenceManager.getString(context, "token");
//        Log.d(TAG, "onCreate Token: " + token);
            //서비스 생성 (항상 헤더에 토큰을 담아서 리퀘스트)
            service = ServiceGenerator.getService(ServerRequestApi.class);
            // 알맞는 request 형식 (여기서는 token) 을 파라미터로 담아서 리퀘스트
//        service.getParkByCurrentLocation(currentLat, currentLng).enqueue(new Callback<ParkList>() {
            service.getParkByCurrentLocation(mlat, mlon).enqueue(new Callback<ParkRes>() { // ( 여기 숫자부분에 GPS 정보 받아와서 넣어주시면 정상 작동할 것 같습니다 )
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

    public void addMarketMarker(List<ParkInfo> marketList) {
        final String TAG = "dlgochan";
        // Marker img -> bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.info);

        for (int i = 0; i < marketList.size(); i++) {

            String storeName = marketList.get(i).getName();     // 이름
            String address = marketList.get(i).getAddr();         // 주소
            double lat = marketList.get(i).getLat();            // 위도
            double lon = marketList.get(i).getLng();           // 경도



            // TMapPoint
            TMapPoint tMapPoint = new TMapPoint(lat, lon);

            // TMapMarkerItem
            // Marker Initial Settings
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);                 // bitmap를 Marker icon으로 사용
            tMapMarkerItem.setPosition(0.5f, 1.0f);         // Marker img의 position
            tMapMarkerItem.setTMapPoint(tMapPoint);         // Marker의 위치
            tMapMarkerItem.setName(storeName);              // Marker의 이름

            // Balloon View Initial Settings
            tMapMarkerItem.setCanShowCallout(true);         // Balloon View 사용
            tMapMarkerItem.setCalloutTitle(storeName);      // Main Message
            tMapMarkerItem.setCalloutSubTitle(address);     // Sub Message
            tMapMarkerItem.setAutoCalloutVisible(false);    // 초기 접속 시 Balloon View X
            tMapMarkerItem.setCalloutRightButtonImage(bitmap2);


            // add Marker on T Map View
            // id로 Marker을 식별
            tMapView.addMarkerItem("marketLocation" + i, tMapMarkerItem);



        }

    }


    @Override
    public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
        final String TAG = "dlgochan";
        // Toast.makeText(this, "풍선뷰 클릭", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "풍선뷰 클릭?");
    }
}