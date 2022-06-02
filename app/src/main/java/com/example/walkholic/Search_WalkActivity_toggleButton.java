package com.example.walkholic;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkholic.DataClass.Data.Road;
import com.example.walkholic.DataClass.Response.RoadRes;
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
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_WalkActivity_toggleButton extends AppCompatActivity implements View.OnClickListener, TMapGpsManager.onLocationChangedCallback, TMapView.OnCalloutRightButtonClickCallback {

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
    Button btn_hashtag_refresh;
    Button btn_hashtag_search;

    ToggleButton btn_hashtag_나들이;
    ToggleButton btn_hashtag_물놀이;
    ToggleButton btn_hashtag_아이와함께;
    ToggleButton btn_hashtag_걷기좋은;
    ToggleButton btn_hashtag_드라이브코스;
    ToggleButton btn_hashtag_데이트코스;
    ToggleButton btn_hashtag_분위기좋은;
    ToggleButton btn_hashtag_런닝;
    ToggleButton btn_hashtag_벚꽃명소;
    ToggleButton btn_hashtag_힐링;

    TextInputEditText textInputEditText;
    Button imageButton;

    boolean checkHashtag[] = new boolean[10];
    String hashtagType[] = {"나들이", "물놀이", "아이와함께", "걷기좋은", "드라이브코스", "분위기좋은", "런닝", "벚꽃명소", "힐링"};

    String API_Key = "l7xxaf0e68fd185f445596200b488c1177af";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;
    //T Map Data
    TMapData tmapdata;

    Double mlat;
    Double mlon;

    private RoadRes roadRes;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_walk);

        Arrays.fill(checkHashtag, false);

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
        FrameLayout linearLayoutTmap = (FrameLayout) findViewById(R.id.linearLayoutTmap_park);
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
        btn_set_location = findViewById(R.id.btn_set_location);
        btn_current_location = findViewById(R.id.btn_current_location);

        btn_hashtag_refresh = findViewById(R.id.btn_hashtag_refresh);
        btn_hashtag_search= findViewById(R.id.btn_hashtag_search);

        imageButton = findViewById(R.id.imageButton);
        textInputEditText = findViewById(R.id.textInputEditText);

        btn_hashtag_나들이 = (ToggleButton) findViewById(R.id.btn_hashtag_나들이);
        btn_hashtag_물놀이 = (ToggleButton) findViewById(R.id.btn_hashtag_물놀이);
        btn_hashtag_아이와함께 = (ToggleButton) findViewById(R.id.btn_hashtag_아이와함께);
        btn_hashtag_걷기좋은 = (ToggleButton) findViewById(R.id.btn_hashtag_걷기좋은);
        btn_hashtag_드라이브코스 = (ToggleButton) findViewById(R.id.btn_hashtag_드라이브코스);
        btn_hashtag_데이트코스 = (ToggleButton) findViewById(R.id.btn_hashtag_데이트코스);
        btn_hashtag_분위기좋은 = (ToggleButton) findViewById(R.id.btn_hashtag_분위기좋은);
        btn_hashtag_런닝 = (ToggleButton) findViewById(R.id.btn_hashtag_런닝);
        btn_hashtag_벚꽃명소 = (ToggleButton) findViewById(R.id.btn_hashtag_벚꽃명소);
        btn_hashtag_힐링 = (ToggleButton) findViewById(R.id.btn_hashtag_힐링);

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
        btn_hashtag_refresh.setOnClickListener(this);
        btn_hashtag_search.setOnClickListener(this);

        btn_hashtag_나들이.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_나들이.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[0] = true;
                    Log.d("dlgochan", Arrays.toString(checkHashtag));
                } else {
                    btn_hashtag_나들이.setTextColor(Color.WHITE);
                    checkHashtag[0] = false;
                    Log.d("dlgochan", Arrays.toString(checkHashtag));
                }
            }
        });
        btn_hashtag_물놀이.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_물놀이.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[1] = true;
                } else {
                    btn_hashtag_물놀이.setTextColor(Color.WHITE);
                    checkHashtag[1] = false;
                }
            }
        });
        btn_hashtag_아이와함께.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_아이와함께.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[2] = true;
                } else {
                    btn_hashtag_아이와함께.setTextColor(Color.WHITE);
                    checkHashtag[2] = false;
                }
            }
        });
        btn_hashtag_걷기좋은.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_걷기좋은.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[3] = true;
                } else {
                    btn_hashtag_걷기좋은.setTextColor(Color.WHITE);
                    checkHashtag[3] = false;
                }
            }
        });
        btn_hashtag_드라이브코스.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_드라이브코스.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[4] = true;
                } else {
                    btn_hashtag_드라이브코스.setTextColor(Color.WHITE);
                    checkHashtag[4] = false;
                }
            }
        });
        btn_hashtag_데이트코스.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_데이트코스.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[5] = true;
                } else {
                    btn_hashtag_데이트코스.setTextColor(Color.WHITE);
                    checkHashtag[5] = false;
                }
            }
        });
        btn_hashtag_분위기좋은.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_분위기좋은.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[6] = true;
                } else {
                    btn_hashtag_분위기좋은.setTextColor(Color.WHITE);
                    checkHashtag[6] = false;
                }
            }
        });
        btn_hashtag_런닝.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_런닝.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[7] = true;
                } else {
                    btn_hashtag_런닝.setTextColor(Color.WHITE);
                    checkHashtag[7] = false;
                }
            }
        });
        btn_hashtag_벚꽃명소.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_벚꽃명소.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[8] = true;
                } else {
                    btn_hashtag_벚꽃명소.setTextColor(Color.WHITE);
                    checkHashtag[8] = false;
                }
            }
        });
        btn_hashtag_힐링.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hashtag_힐링.setTextColor(Color.parseColor("#FFFFCC"));
                    checkHashtag[9] = true;
                } else {
                    btn_hashtag_힐링.setTextColor(Color.WHITE);
                    checkHashtag[9] = false;
                }
            }
        });

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
                Intent intent3 = new Intent(getApplicationContext(), WalkingActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.btn_mypage:
                Intent intent4 = new Intent(this, WalkListActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.btn_search_park:
                Intent intent5 = new Intent(this, Search_ParkActivity.class);
                startActivity(intent5);
                finish();
                break;
//            case R.id.btn_search_walk:
//                Intent intent6 = new Intent(this, Search_WalkActivity.class);
//                startActivity(intent6);
//                finish();
//                break;
            case R.id.btn_search_shared:
                Intent intent7 = new Intent(this, Search_SharedActivity.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.btn_set_location:
                Log.d("dlgochan", "위도: " + mlat + "경도: " + mlon);
                getRoadByCurrentLocation(mlat, mlon);
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
                        intent8.putExtra("road", true);
                        startActivity(intent8);
                    }
                }, 500);
            case R.id.btn_current_location:
                TrackingMode = true;
                Log.d("dlgochan", "새로고침 버튼 클릭!");
            case R.id.btn_hashtag_refresh:
                btn_hashtag_나들이.setChecked(false);
                btn_hashtag_물놀이.setChecked(false);
                btn_hashtag_아이와함께.setChecked(false);
                btn_hashtag_걷기좋은.setChecked(false);
                btn_hashtag_드라이브코스.setChecked(false);
                btn_hashtag_데이트코스.setChecked(false);
                btn_hashtag_분위기좋은.setChecked(false);
                btn_hashtag_런닝.setChecked(false);
                btn_hashtag_벚꽃명소.setChecked(false);
                btn_hashtag_힐링.setChecked(false);
                Arrays.fill(checkHashtag, false);
            case R.id.btn_hashtag_search:
                List<String> hashtags = new ArrayList<String>();
                for (int i = 0; i < checkHashtag.length; i++) {
                    if (checkHashtag[i] == true) {
                        hashtags.add(hashtagType[i]);
                    }
                }
        }
    }

    @Override
    public void onLocationChange(Location location) {

        if (TrackingMode) {
            mlat = location.getLatitude();
            mlon = location.getLongitude();

            Log.e("dlgochan", "위치변경 탐지: " + mlat + " / " + mlon);
            //원래 2줄만 있던 코드, 좌표 변경 시 좌표 기록을 해보자
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        }

    }

    public void getRoadByCurrentLocation(double lat, double lng){
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getRoadByCurrentLocation(lat, lng).enqueue(new Callback<RoadRes>() {
            @Override
            public void onResponse(Call<RoadRes> call, Response<RoadRes> response) {
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    roadRes = response.body();
                    Log.d(TAG, "onResponse Success : " + roadRes.toString());
                    addMarketMarker(roadRes.getData());
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
            public void onFailure(Call<RoadRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });

    }

    public void addMarketMarker(List<Road> marketList) {
        final String TAG = "dlgochan";
        // Marker img -> bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.info);
        try {
            for (int i = 0; i < marketList.size(); i++) {

                String storeName = marketList.get(i).getRoadName();     // 이름
                String address = marketList.get(i).getStartRoadAddr();
                double lat = marketList.get(i).getStartLat();            // 위도
                double lon = marketList.get(i).getStartLng();           // 경도
                Integer roadId = marketList.get(i).getId();

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
                tMapMarkerItem.setCalloutRightButtonImage(bitmap2); //구름뷰 오른쪽 비트맨 클릭시 onCalloutRightButton호출

                // add Marker on T Map View
                // id로 Marker을 식별
                tMapView.addMarkerItem(roadId.toString(), tMapMarkerItem);

            }
        } catch (NullPointerException e) {
            Log.d(TAG, "onFailure : " + e.getMessage());
        }
    }


    @Override
    public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
        final String TAG = "dlgochan";
        // Toast.makeText(this, "풍선뷰 클릭", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "풍선뷰 roadId: " + tMapMarkerItem.getID());
        Integer roadId = Integer.parseInt(tMapMarkerItem.getID());

        Intent intent = new Intent(getApplicationContext(), RoadCloudviewHomeActivity.class);
        intent.putExtra("lat", mlat);
        intent.putExtra("lng", mlon);
        intent.putExtra("roadId", roadId);
        startActivity(intent);
    }

}