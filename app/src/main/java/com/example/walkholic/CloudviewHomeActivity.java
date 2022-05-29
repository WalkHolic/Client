package com.example.walkholic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Data.MyTMapMarkerItem;
import com.example.walkholic.DataClass.Data.ParkInfo;
import com.skt.Tmap.TMapMarkerItem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CloudviewHomeActivity extends AppCompatActivity implements View.OnClickListener{


    final String TAG = "dlgochan";

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;
    Button btn_back;


    URL url;
    Bitmap bitmap;


    ImageView parkimageview;
    String name;
    String type;
    String contact;
    String manageAgency;
    String AddrNew;
    String Addr;
    String ParkId;

    TextView txt_name;
    TextView txt_type;
    TextView txt_contact;
    TextView txt_manageAgency;
    TextView txt_addrNew;
    TextView txt_addr;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloudview_home);





        Intent intent = getIntent();
        MyTMapMarkerItem parkInfo = (MyTMapMarkerItem) intent.getSerializableExtra("park");
        try {
            Log.d(TAG, "풍선뷰 정보 :  " + parkInfo.toString());
        }catch (Exception e){
            Log.d(TAG, "풍선뷰 정보를 받아오지 못했습니다");
        }



        name = parkInfo.getName();
        type = parkInfo.getType();
        contact = parkInfo.getContact();
        manageAgency = parkInfo.getManageAgency();
        AddrNew = parkInfo.getAddrNew();
        Addr = parkInfo.getAddr();
        ParkId = parkInfo.getParkId();






        if(name == null){
            name = "공원 이름이 아직 없습니다";
        }

        if(type == null){
            type = "공원 타입이 등록되지 않았습니다";
        }

        if(contact == null){
            contact = "공원 연락처가 등록되지 않았습니다";
        }

        if(manageAgency == null){
            manageAgency = "공원 관리소가 등록되지 않았습니다";
        }

        if(AddrNew == null){
            AddrNew = "공원 도로명주소가 등록되지 않았습니다";
        }

        if(Addr == null){
            Addr = "공원 지번주소가 등록되지 않았습니다";
        }


        parkimageview = findViewById(R.id.parkimageView);
        txt_name = findViewById(R.id.txt_name);
        txt_type = findViewById(R.id.txt_type);
        txt_contact = findViewById(R.id.txt_contact);
        txt_manageAgency = findViewById(R.id.txt_manageAgency);
        txt_addrNew = findViewById(R.id.txt_addrNew);
        txt_addr = findViewById(R.id.txt_addr);


        /*try {
            new Thread(() -> {
                try {
                    url = new URL(parkInfo.getPngPath());
                    Log.d(TAG, "URL :  " + url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }).start();
            Glide.with(this).load(parkInfo.getPngPath()).into(parkimageview);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Glide.with(this).load(parkInfo.getPngPath()).into(parkimageview);



        txt_name.setText(name);
        txt_type.setText(type);
        txt_contact.setText(contact);
        txt_manageAgency.setText(manageAgency);
        txt_addrNew.setText(AddrNew);
        txt_addr.setText(Addr);

        Log.d(TAG, "풍선뷰 정보 :  " + name + type + contact + manageAgency + AddrNew + Addr + ParkId );
       // Log.d(TAG, "풍선뷰 정보 :  " + parkInfo.getPngPath() );









        btn_home =  findViewById(R.id.btn_home);
        btn_search =  findViewById(R.id.btn_search);
        btn_walking =  findViewById(R.id.btn_walking);
        btn_mypage =  findViewById(R.id.btn_mypage);
        btn_back = findViewById(R.id.back_btn);




        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);
        btn_back.setOnClickListener(this);



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
            case R.id.back_btn:
                onBackPressed();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}