package com.example.walkholic;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.walkholic.DTO.ParkList;
import com.example.walkholic.DTO.UserList;
import com.example.walkholic.Service.PreferenceManager;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParkActivity extends AppCompatActivity implements View.OnClickListener,  TMapGpsManager.onLocationChangedCallback {

    ImageButton btn_home;
    ImageButton btn_park;
    ImageButton btn_walk;
    ImageButton btn_walking;
    ImageButton btn_walk_list;

    String API_Key = "l7xxaf0e68fd185f445596200b488c1177af";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;
    //T Map Data
    TMapData tmapdata;

    private Context context; // 이해찬 추가
    private ParkList parkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        //이해찬 추가
        /////////////////////////////////////////////////////////////////////////
        final String TAG = "dlgochan";
        // 안드로이드 앱 내부 파일 (SharedPreference) 에서 jwt 값 가져오기
        context = this;
        String token = PreferenceManager.getString(context, "token");
        Log.d(TAG, "onCreate Token: " + token);
        //서비스 생성 (항상 헤더에 토큰을 담아서 리퀘스트)
        ServerRequestApi service = ServiceGenerator.createService(ServerRequestApi.class, token);
        // 알맞는 request 형식 (여기서는 token) 을 파라미터로 담아서 리퀘스트
//        service.getParkByCurrentLocation(currentLat, currentLng).enqueue(new Callback<ParkList>() {
        service.getParkByCurrentLocation(37.24815347, 126.9648203).enqueue(new Callback<ParkList>() { // ( 여기 숫자부분에 GPS 정보 받아와서 넣어주시면 정상 작동할 것 같습니다 )
            @Override
            public void onResponse(Call<ParkList> call, Response<ParkList> response) { // Call<타입> : 타입을 잘 맞춰주시면 됩니다. ex) 산책로 조회는 RoadList, 산책로 경로 조회는 RoadPath
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    parkList = response.body();
                    Log.d(TAG, "onResponse Success : " + parkList.toString());
                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d(TAG, "onResponse Fail : " + response.message());
                    Log.d(TAG, String.format("onResponse Fail : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ParkList> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());

            }
        });
        // parkList 사용법 (DTO의 ParkList 참고)
//        parkList.getData().get(i).getLat() // 리스트의 i번째 공권의 위도
//        parkList.getData().get(i).getLng() // 리스트의 i번째 공원의 경도
        /////////////////////////////////////////////////////////////////////////



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
        tMapView.setLocationPoint(tMapGPS.getLocation().getLongitude(), tMapGPS.getLocation().getLatitude());
        tMapView.setCenterPoint(tMapGPS.getLocation().getLongitude(), tMapGPS.getLocation().getLatitude());













        btn_home =  findViewById(R.id.btn_home);
        btn_park =  findViewById(R.id.btn_park);
        btn_walk =  findViewById(R.id.btn_walk);
        btn_walking = findViewById(R.id.btn_walking);
        btn_walk_list = findViewById(R.id.btn_walk_list);

        btn_home.setOnClickListener(this);
        btn_park.setOnClickListener(this);
        btn_walk.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_walk_list.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_park:
                Intent intent2 = new Intent(getApplicationContext(), ParkActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_walk:
                Intent intent3 = new Intent(getApplicationContext(), WalkActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.btn_walking:
                Intent intent4 = new Intent(getApplicationContext(), WalkingActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.btn_walk_list:
                Intent intent5 = new Intent(getApplicationContext(), WalkListActivity.class);
                startActivity(intent5);
                finish();
                break;

        }
    }

    @Override
    public void onLocationChange(Location location) {

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
}