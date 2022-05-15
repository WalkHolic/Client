package com.example.walkholic;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    /*private TextView tv_nickname;
    private ImageView iv_profile;
    private FirebaseAuth auth;*/

    //네비게이션 바
    BottomNavigationView bottomNavigationView;
    //fragment
    FragHome fragHome;
    FragSearch fragSearch;
    FragPlay fragPlay;
    FragList fragList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent intent = getIntent();
        String nickname = intent.getStringExtra("nicmName");
        String photourl = intent.getStringExtra("photoUrl");

       *//* Log.d("asdasd", nickname);
        Log.d("asdasd", photourl);*//*

        Bundle bundle = new Bundle();
        bundle.putString("nickname", nickname);
        bundle.putString("photoUrl", photourl);*/


        //네비게이션 바
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //fragment
        fragHome = new FragHome();
        /*fragHome.setArguments(bundle);*/

        fragSearch = new FragSearch();
        fragPlay = new FragPlay();
        fragList = new FragList();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragHome).commitAllowingStateLoss();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                    case R.id.tab_home: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragHome).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab_search: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragSearch).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab_play: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragPlay).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab_list: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragList).commitAllowingStateLoss();
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

    }
}