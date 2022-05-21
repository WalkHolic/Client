package com.example.walkholic;

import android.Manifest;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);


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