package com.example.walkholic;

import static java.lang.Thread.sleep;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.walkholic.DataClass.DTO.UserRoadRequestDto;
import com.example.walkholic.DataClass.Data.Road;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WalkingActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback, TMapView.OnClickListenerCallback, TMapView.OnLongClickListenerCallback, View.OnClickListener {
    //UID 예시 아마 안쓸거임
    private long uid = 1;

    UserRoadRequestDto road;

    Dialog dialog2;
    AlertDialog dialog;

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;

    Button btn_hash;

    ListViewAdapter adapter;

    Retrofit retrofit;
    ServerRequestApi severRequestApi;
    Call<List<Road>> call;

    String API_Key = "l7xxaf0e68fd185f445596200b488c1177af";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;
    //T Map Data
    TMapData tmapdata;

    // 신규 경로
    Trail newTrail = null;

    DBHelper dbHelper;

    boolean isRecording = false;
    boolean isDrawing = false;

    String trailPoints = "";

    private UserRoadRes userRoadRes;

    private String address1 = "aa";
    private String address2 = "bb";

    boolean drawSwitchFlag = false;
    Switch drawSwitch;
    int counter = 0;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    FloatingActionButton fab, fab1, fab2, fab3;
    TextView fab1_text, fab2_text, fab3_text;
    Button recordButton, drawBackButton, clearButton, drawButton;
    List<CheckBox> checkBoxes;

    //산책로이름, 산책로 설명, 해시태그
    private String road_name = "새 산책로";
    private String road_desc = "빈 설명";
    private String road_hash = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adapter = new ListViewAdapter();

        road = new UserRoadRequestDto();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        dbHelper = new DBHelper(this, 1);


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
        LinearLayout linearLayoutTmap = findViewById(R.id.linearLayoutTmap_walking);
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
        Log.d("helllllo", "long= " + tMapGPS.getLocation().getLongitude() + "lat= " + tMapGPS.getLocation().getLatitude());


        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);


        clearButton = findViewById(R.id.clearButton);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);


        fab1_text = findViewById(R.id.fab1_text);
        fab2_text = findViewById(R.id.fab2_text);
        fab3_text = findViewById(R.id.fab3_text);

        recordButton = findViewById(R.id.recordButton);
        drawBackButton = findViewById(R.id.drawBackButton);
        drawButton = findViewById(R.id.drawButton);
        drawSwitch = findViewById(R.id.switch1);


        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);

        clearButton.setOnClickListener(this);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        recordButton.setOnClickListener(this);
        drawBackButton.setOnClickListener(this);
        drawButton.setOnClickListener(this);

        // activity_main의 버튼을 가져오기
        Button shareButton = findViewById(R.id.shareButton);

        // 클릭 리스너 구현
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenShot();
                Log.d("holla", "마지막");
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
            case R.id.fab:
                anim();
                break;
            case R.id.fab1: // 그려진 경로 지우기
                anim();
                clearTrail();
                break;
            case R.id.fab2: // 터치로 그리기
                anim();
                //그리기 시작
                if (!isDrawing && !isRecording) {
                    drawButton.setVisibility(View.VISIBLE);
                    drawBackButton.setVisibility(View.VISIBLE);
                    //drawSwitch.setVisibility(View.VISIBLE);
                    clearTrail();

                    Toast.makeText(getApplicationContext(), "그리기 시작", Toast.LENGTH_SHORT).show();
                    isDrawing = true;
                    newTrail = new Trail();
                    newTrail.coorList.add(tMapGPS.getLocation());
                    TMapPoint point1 = newTrail.coorList.get(0);
                    Log.d("TmapTest", "" + point1.getLatitude());
                    Log.d("TmapTest", "" + point1.getLongitude());
                    tmapdata.convertGpsToAddress(point1.getLatitude(), point1.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String addr) {
                            address1 = addr;
                            Log.d("TmapTest", "*** updatePositionInfo - addr: " + addr);
                        }
                    });
                }


                break;

            case R.id.drawButton: // 터치로그리기 종료하기
                drawButton.setVisibility(View.GONE);
                drawBackButton.setVisibility(View.GONE);
                //drawSwitch.setVisibility(View.GONE);
                //그리기 종료
                if (isDrawing) {

                    /*AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setTitle("산책로의 이름을 지정해주세요");*/
                    Toast.makeText(getApplicationContext(), "그리기 종료", Toast.LENGTH_SHORT).show();
                    drawTrail(newTrail);
                    newTrail.calTotalDistance();
                    isDrawing = false;
                    // db초기화

                    //dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 1);
                    try {
                        TMapPoint point2 = newTrail.coorList.get(newTrail.coorList.size() - 1);
                        Log.d("TmapTest", "" + point2.getLatitude());
                        Log.d("TmapTest", "" + point2.getLongitude());

                        tmapdata.convertGpsToAddress(point2.getLatitude(), point2.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                            @Override
                            public void onConvertToGPSToAddress(String addr) {
                                Log.d("TmapTest", "*** updatePositionInfo - addr: " + addr);
                            }
                        });
                        //address = tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude());

                    } catch (Exception e) {
                        Log.d("error", "*** Exception: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), "총 거리 : " + newTrail.totalDistance + "km", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "시작주소 : " + address1 , Toast.LENGTH_SHORT).show();

                    try {
                        if (newTrail.totalDistance != 0)
                            //dbHelper.insert(uid, road_name, road_desc, newTrail.coorList, newTrail.totalDistString, address1);
                            showDialog(newTrail);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getApplicationContext(), "디비저장\n" + dbHelper.getResult() , Toast.LENGTH_LONG).show();


                    newTrail = null;
                } else {
                    Toast.makeText(getApplicationContext(), "문제", Toast.LENGTH_LONG).show();
                    return;
                }

                break;


            case R.id.fab3: // 기록하기
                anim();
                recordButton.setVisibility(View.VISIBLE);

                //기록 새로 시작하기
                clearTrail();

                Toast.makeText(getApplicationContext(), "기록 시작", Toast.LENGTH_SHORT).show();
                isRecording = true;
                newTrail = new Trail();
                newTrail.coorList.add(tMapGPS.getLocation());
                TMapPoint point1 = newTrail.coorList.get(0);
                Log.d("TmapTest", "" + point1.getLatitude());
                Log.d("TmapTest", "" + point1.getLongitude());
                tmapdata.convertGpsToAddress(point1.getLatitude(), point1.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String addr) {
                        address1 = addr;
                        Log.d("TmapTest", "*** updatePositionInfo - addr: " + addr);
                    }
                });


                break;

            case R.id.recordButton: // 기록 완료
                recordButton.setVisibility(View.GONE);
                //기록 중에 기록을 종료하기

                Toast.makeText(getApplicationContext(), "기록 종료", Toast.LENGTH_SHORT).show();
                isRecording = false;
                newTrail.coorList.add(tMapGPS.getLocation());
                drawTrail(newTrail);
                //dbHelper.insert("새산책로", newTrail.coorList,);
                newTrail.calTotalDistance();
                showDialog(newTrail);

                break;

            case R.id.drawBackButton: // 되돌리기
                newTrail.coorList.remove(newTrail.coorList.size() - 1);
                drawTrail(newTrail);
                break;

        }
    }

    // 빠르게 테스트 필요
    public void createMyRoad(UserRoadRequestDto roadRequestDto) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.createMyRoad(roadRequestDto).enqueue(new Callback<UserRoadRes>() {
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

    @Override
    public void onLocationChange(Location location) {

        Log.e("asdasd", "위치변경");
        //원래 2줄만 있던 코드, 좌표 변경 시 좌표 기록을 해보자
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        //만약 좌표를 기록 중이라면,

        if (isRecording) {
            //Toast.makeText(getApplicationContext(), "좌표 기록 중", Toast.LENGTH_SHORT).show();
            newTrail.coorList.add(tMapGPS.getLocation());
            drawTrail(newTrail);
        }
        /*TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();

        Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.marker_blue);
        tMapMarkerItem.setIcon(markerIcon); // 마커 아이콘 지정

        tMapMarkerItem.setTMapPoint(new TMapPoint(location.getLongitude(), location.getLatitude()));
        tMapMarkerItem.setName("marker");
        tMapView.addMarkerItem("marker", tMapMarkerItem);*/

    }

    public void pointsToString(Trail inTrail) {
        Trail tempTrail = inTrail;
        trailPoints = trailPoints + "[";
        for (int i = 0; i < tempTrail.coorList.size(); i++) {
            if (i != 0) trailPoints += ",";
            trailPoints = trailPoints + "[" + tempTrail.coorList.get(i).getLatitude() + "," + tempTrail.coorList.get(i).getLongitude() + "]";
        }
        trailPoints = trailPoints + "]";
        Log.d("json Test", "list : " + trailPoints);
    }

    public List<List<Double>> pointsToList(Trail inTrail) {
        Trail tempTrail = inTrail;
        List<List<Double>> tempList1 = new ArrayList<>();
        for (int i = 0; i < tempTrail.coorList.size(); i++) {
            List<Double> tempList2 = new ArrayList<>();
            tempList2.add(tempTrail.coorList.get(i).getLatitude());
            tempList2.add(tempTrail.coorList.get(i).getLongitude());
            tempList1.add(tempList2);
            //Log.d("json Test", "list2 : " + tempList2.toString());
            //Log.d("json Test", "list3 : " + tempList1.toString());

        }
        Log.d("json Test", "list4 : " + tempList1.toString());
        return tempList1;
    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1_text.startAnimation(fab_close);
            fab2_text.startAnimation(fab_close);
            fab3_text.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1_text.startAnimation(fab_open);
            fab1_text.setVisibility(View.VISIBLE);
            fab2_text.startAnimation(fab_open);
            fab2_text.setVisibility(View.VISIBLE);
            fab3_text.startAnimation(fab_open);
            fab3_text.setVisibility(View.VISIBLE);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
    }


    // 기록 종료된 산책로 그리기
    public void drawTrail(Trail inTrail) {
        ArrayList<TMapPoint> tempList = inTrail.coorList;

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.BLUE);
        tMapPolyLine.setLineWidth(2);
        for (int i = 0; i < tempList.size(); i++) {
            tMapPolyLine.addLinePoint(tempList.get(i));
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine);
    }


    public void drawTrail2(Trail inTrail) throws IOException, ParserConfigurationException, SAXException {

        new Thread(() -> {
            ArrayList<TMapPoint> tempList = inTrail.coorList;
            TMapPoint tMapPointStart;
            TMapPoint tMapPointEnd;
            Log.d("TmapTest", "drawTrail2");
            for (int i = 0; i < tempList.size() - 1; i++) {
                tMapPointStart = tempList.get(i);
                tMapPointEnd = tempList.get(i + 1);

                TMapPolyLine tMapPolyLine = null;
                try {
                    tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                tMapPolyLine.setLineColor(Color.RED);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("Line" + i, tMapPolyLine);
                try {
                    Thread.sleep(600);
                } catch (Exception e) {

                }

            }
        }).start();
        Log.d("TmapTest", "drawTrail2 end");
    }


    public void drawTrail3(Trail inTrail) throws IOException, ParserConfigurationException, SAXException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<TMapPoint> tempList = inTrail.coorList;
                TMapPoint tMapPointStart;
                TMapPoint tMapPointEnd;
                while (true) {
                    for (int i = 0; i < tempList.size() - 1; i++) {
                        tMapPointStart = tempList.get(i);
                        tMapPointEnd = tempList.get(i + 1);

                        TMapPolyLine tMapPolyLine = null;
                        try {
                            tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        tMapPolyLine.setLineColor(Color.RED);
                        tMapPolyLine.setLineWidth(2);
                        tMapView.addTMapPolyLine("Line1", tMapPolyLine);

                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Log.d("TmapTest", "drawTrail2 end");
    }

    public void clearTrail() {
        Toast.makeText(getApplicationContext(), "지우기 성공", Toast.LENGTH_SHORT).show();
        tMapView.removeAllTMapPolyLine();
        tMapView.removeAllTMapCircle();
    }

    private void showDialog2() {

    }

    private void showDialog(Trail inTrail) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.dialog, null);

        final EditText name = loginLayout.findViewById(R.id.road_name);
        final EditText desc = loginLayout.findViewById(R.id.road_desc);
        final EditText hash = loginLayout.findViewById(R.id.road_hash);
        final TextView hashlist = loginLayout.findViewById(R.id.hashText);

        final List<String> tempString = new ArrayList<String>();
        final String[] hashs = new String[]{"나들이", "물놀이", "아이와함께", "걷기좋은", "드라이브코스", "데이트코스", "분위기좋은", "런닝", "벚꽃명소", "힐링"};
        final String[] textTags = {""};
        btn_hash = loginLayout.findViewById(R.id.btn_hash);

        btn_hash.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "버튼클릭", Toast.LENGTH_SHORT).show();
                // TODO : click event
                new AlertDialog.Builder(loginLayout.getContext()).setTitle("해시태그 선택")
                        .setMultiChoiceItems(hashs, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                        if (b) {
                                            tempString.add(hashs[i]);
                                        } else {
                                            tempString.remove(i);
                                        }
                                    }
                                }
                        )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (String temp : tempString
                                ) {
                                    textTags[0] = textTags[0] + "#" + temp + " ";
                                }
                                hashlist.setText(textTags[0]);
                                textTags[0]="";
                            }
                        }).show();

            }
        });

        new AlertDialog.Builder(this).setTitle("산책로 정보입력").setView(loginLayout)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        road_name = name.getText().toString();
                        road_desc = desc.getText().toString();
                        //road_hash = hash.getText().toString();
                        try {
                            TMapPoint point2 = inTrail.coorList.get(inTrail.coorList.size() - 1);
                            Log.d("TmapTest", "" + point2.getLatitude());
                            Log.d("TmapTest", "" + point2.getLongitude());

                            tmapdata.convertGpsToAddress(point2.getLatitude(), point2.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                                @Override
                                public void onConvertToGPSToAddress(String addr) {
                                    Log.d("TmapTest", "*** updatePositionInfo - addr: " + addr);
                                }
                            });
                            //address = tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude());

                        } catch (Exception e) {
                            Log.d("error", "*** Exception: " + e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), "총 거리 : " + newTrail.totalDistance + "km", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "시작주소 : " + address1 , Toast.LENGTH_SHORT).show();
                        try {

                            if (inTrail.totalDistance != 0)
                                //dbHelper.insert(uid, road_name, road_desc, newTrail.coorList, newTrail.totalDistString, address1);
                                road.setTrailName(road_name);
                            road.setDescription(road_desc);
                            road.setDistance(inTrail.totalDistance);
                            road.setStartAddr(address1);
                            road.setTrailPoints(pointsToList(inTrail));
                            //tempTag.add(road_hash);
                            road.setHashtag(tempString);
                            //pointsToString(inTrail);
                            createMyRoad(road);
                            Log.d("TmapTest", "*** name,desc,hash : " + road_name + ", " + road_desc + ", " + road_hash);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        newTrail = null;
                    }
                }).show();
    }

    //화면 캡쳐하기
    public void ScreenShot() {

        View view = getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다
        Log.d("holla", "1");
        //캐시를 비트맵으로 변환
        Bitmap screenBitmap = Bitmap.createBitmap(view.getDrawingCache());
        Log.d("holla", "2");
        try {

            File cachePath = new File(getApplicationContext().getCacheDir(), "images");
            Log.d("holla", "3");
            cachePath.mkdirs(); // don't forget to make the directory
            Log.d("holla", "4");
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            Log.d("holla", "5");
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Log.d("holla", "6");
            stream.close();

            File newFile = new File(cachePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                    "com.example.walkholic.fileprovider", newFile);

            Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
            Sharing_intent.setType("image/png");
            Sharing_intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(Sharing_intent, "Share image"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //터치로 경로 그리기
    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return false;
    }

    @Override
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return false;
    }

    public void drawLine1(TMapPoint a, TMapPoint b) throws IOException, ParserConfigurationException, SAXException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                TMapPolyLine tMapPolyLine = null;

                try {
                    tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, a, b);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                tMapPolyLine.setLineColor(Color.RED);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.d("TmapTest", "drawLine 1 + " + drawSwitch);
    }

    public void drawLine2(TMapPoint a, TMapPoint b) throws IOException, ParserConfigurationException, SAXException {
        TMapPolyLine tMapPolyLine = null;
        tMapPolyLine = new TMapData().findPathData(a, b);
        tMapPolyLine.setLineColor(Color.RED);
        tMapPolyLine.setLineWidth(2);
        tMapView.addTMapPolyLine("Line", tMapPolyLine);
        Log.d("TmapTest", "drawLine 2 + " + drawSwitch);
    }

    @Override
    public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
        if (isDrawing) {
            if (drawSwitchFlag) { // 길찾기로 그리기
                try {
                    drawLine1(newTrail.coorList.get(newTrail.coorList.size() - 1), tMapPoint);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            } else { // 선으로 그리기
                try {
                    drawLine2(newTrail.coorList.get(newTrail.coorList.size() - 1), tMapPoint);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
            newTrail.coorList.add(tMapPoint);
            Toast.makeText(getApplicationContext(), "터치기록", Toast.LENGTH_SHORT).show();

            TMapCircle tMapCircle = new TMapCircle();
            tMapCircle.setCenterPoint(tMapPoint);
            tMapCircle.setRadius(3);
            tMapCircle.setCircleWidth(3);
            tMapCircle.setLineColor(Color.RED);
            tMapCircle.setAreaColor(Color.RED);
            tMapCircle.setAreaAlpha(0);
            /*try {
                Log.d("TmapTest", "drawTrail2 call");
                drawTrail2(newTrail);
                Log.d("TmapTest", "drawTrail2 done");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }*/
            tMapView.addTMapCircle("circle1", tMapCircle);
            drawTrail(newTrail);
        } else {
            //Toast.makeText(getApplicationContext(), "기록안하는중", Toast.LENGTH_SHORT).show();
        }
    }//functions end

}