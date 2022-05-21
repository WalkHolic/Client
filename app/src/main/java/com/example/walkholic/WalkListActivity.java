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
import android.widget.ImageButton;
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

public class WalkListActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn_home;
    ImageButton btn_park;
    ImageButton btn_walk;
    ImageButton btn_walking;
    ImageButton btn_walk_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_list);

        btn_home = findViewById(R.id.btn_home);
        btn_park = findViewById(R.id.btn_park);
        btn_walk = findViewById(R.id.btn_walk);
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
}