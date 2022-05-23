package com.example.walkholic;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
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
import com.google.firebase.auth.FirebaseUser;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn_home;
    ImageButton btn_park;
    ImageButton btn_walk;
    ImageButton btn_walking;
    ImageButton btn_walk_list;
    private FirebaseAuth auth;
    private String name;
    private Uri photoUrl;
    private TextView tv_nickname;
    private ImageView iv_profile;
    private TextView dayinfo;
    private Context context; // 이해찬 추가
    Button btn_logout;
    Button btn_revoke;

    private ServerRequestApi testService; // Service 요청 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this; // 이해찬 추가
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        tv_nickname = findViewById(R.id.nickName);
        tv_nickname.setText(name);
        iv_profile = findViewById(R.id.profile_Image);
        Glide.with(this)
                .load(photoUrl)
                .into(iv_profile);
        dayinfo = findViewById(R.id.dateInfo);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new  SimpleDateFormat("E dd", new Locale("en", "US"));
        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("en", "US"));
        Log.d("asdasd", month);
        String strDate = dateFormat.format(date)+ " " + month;
        Log.d("asdasd", strDate);
        dayinfo.setText(strDate);










        btn_home =  findViewById(R.id.btn_home);
        btn_park =  findViewById(R.id.btn_park);
        btn_walk =  findViewById(R.id.btn_walk);
        btn_walking =  findViewById(R.id.btn_walking);
        btn_walk_list = findViewById(R.id.btn_walk_list);
        btn_logout = findViewById(R.id.btn_logout);
        btn_revoke = findViewById(R.id.btn_revoke);

        btn_home.setOnClickListener(this);
        btn_park.setOnClickListener(this);
        btn_walk.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_walk_list.setOnClickListener(this);

        btn_logout.setOnClickListener(this);
        btn_revoke.setOnClickListener(this);











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
            case R.id.btn_logout:
                signOut();
                finishAffinity();
                Toast.makeText(this,"로그아웃에 성공했습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_revoke:
                revokeAccess();
                finishAffinity();
                Toast.makeText(this, "회원탈퇴에 성공했습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        auth.getCurrentUser().delete();
    }
}