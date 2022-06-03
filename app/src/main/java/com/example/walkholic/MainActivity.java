package com.example.walkholic;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.walkholic.Service.ServerRequestApi;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , SensorEventListener {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;

    private FirebaseAuth auth;
    private String name;
    private Uri photoUrl;
    private TextView tv_nickname;
    private ImageView iv_profile;
    private TextView dayinfo;
    private Context context; // 이해찬 추가
    Button btn_logout;
    Button btn_revoke;

    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView;
    TextView cals;
    ProgressBar progBar;
    // 현재 걸음 수
    int currentSteps;
    double tt = 0;

    private ServerRequestApi testService; // Service 요청 변수

    @Override
    protected void onResume() {

        super.onResume();
        stepCountView = findViewById(R.id.stepCountView);
        progBar = findViewById(R.id.progressBar1);
        currentSteps = preferences.getInt("step", 0);
        stepCountView.setText(String.valueOf(currentSteps) + " / 10000 steps");
        tt = (int) (currentSteps * 0.035);
        cals.setText(String.valueOf(tt) + " kcal");
        Log.d("asdasd", "onResume");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progBar = findViewById(R.id.progressBar1);
        stepCountView = findViewById(R.id.stepCountView);

        cals = findViewById(R.id.cals);

        //기본 SharedPreferences 환경과 관련된 객체를 얻어옵니다.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();
        currentSteps = preferences.getInt("step", 0);
        progBar.setProgress((int) currentSteps / 100);

        // 활동 퍼미션 체크
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
            Log.d("asdasd", "sensor1");
        } else {
            Log.d("asdasd", "sensor2");
        }

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
        iv_profile.setClipToOutline(true);
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
        btn_search =  findViewById(R.id.btn_search);
        btn_walking =  findViewById(R.id.btn_walking);
        btn_mypage =  findViewById(R.id.btn_mypage);

        btn_logout = findViewById(R.id.btn_logout);
        btn_revoke = findViewById(R.id.btn_revoke);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);

        btn_logout.setOnClickListener(this);
        btn_revoke.setOnClickListener(this);


    }


    public void onStart() {
        super.onStart();
        if (stepCountSensor != null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            //
            // Log.d("asdasd", "sensor3");
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                editor.putInt("step", currentSteps);
                editor.commit();
                tt = (int) (currentSteps * 0.035);
                stepCountView.setText(String.valueOf(currentSteps) + " / 10000 steps");
                progBar.setProgress((int) currentSteps / 10);
                cals.setText(String.valueOf(tt) + " kcal");
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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