package com.example.walkholic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragHome extends Fragment implements View.OnClickListener {

    private View view;
    private MainActivity activity;
    private FirebaseAuth auth;


    private String name;
    private Uri photoUrl;
    private TextView tv_nickname;
    private ImageView iv_profile;
    private TextView dayinfo;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참조가안됨
        activity = null;
    }

    @SuppressLint("WrongThread")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트 메인을 인플레이트해주고 컨테이너에 붙여달라는 뜻임
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_home, container, false);

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
        tv_nickname = rootView.findViewById(R.id.nickName);
        tv_nickname.setText(name);
        iv_profile = rootView.findViewById(R.id.profile_Image);
        //iv_profile.setImageURI(photoUrl);

        Glide.with(this)
                .load(photoUrl)
                .into(iv_profile);
        Log.d("asdasd", String.valueOf(photoUrl));

        dayinfo = rootView.findViewById(R.id.dateInfo);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new  SimpleDateFormat("E dd", new Locale("en", "US"));
        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("en", "US"));
        Log.d("asdasd", month);
        String strDate = dateFormat.format(date)+ " " + month;
        Log.d("asdasd", strDate);
        dayinfo.setText(strDate);

        //버튼 리스너
        Button btn_logout = rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        Button btn_revoke = rootView.findViewById(R.id.btn_revoke);
        btn_revoke.setOnClickListener(this);









        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                signOut();
                activity.finishAffinity();
                Toast.makeText(activity, "로그아웃에 성공했습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_revoke:
                revokeAccess();
                activity.finishAffinity();
                Toast.makeText(activity, "회원탈퇴에 성공했습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
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
