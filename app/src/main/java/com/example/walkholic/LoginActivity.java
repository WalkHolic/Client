package com.example.walkholic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.example.walkholic.DTO.UserRes;
import com.example.walkholic.Service.PreferenceManager;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth auth;  // Firebase 인증 객체
    private GoogleApiClient googleApiClient; // 구글 api 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드
    private Context context; // 이해찬 추가
    TextView textView;
    Retrofit retrofit;
    ServerRequestApi severRequestApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this; // 이해찬 추가
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance(); // Firebase 인증 객체 초기화

        btn_google = findViewById(R.id.google_login_button);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asdasd", "click");
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);*/
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("asdasd", String.valueOf(result.isSuccess()));
            Log.d("asdasd", String.valueOf(requestCode));
            if (result.isSuccess()) { // 인증 결과가 성공

                GoogleSignInAccount account = result.getSignInAccount(); // 구글 로그인 정보
                resultLoogin(account);
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);


            }
        }
    }

    private void resultLoogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("nickName", account.getDisplayName());
                            intent.putExtra("photoUrl", String.valueOf(account.getPhotoUrl()));

                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void send(@Nullable GoogleSignInAccount account) {

        final String TAG = "dlgochan";
        String idtoken = account.getIdToken();
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), idtoken);
        //이해찬 추가
        /////////////////////////////////////////////////////////////////////////
        //서비스 생성
        retrofit = new Retrofit.Builder()
                .baseUrl("https://walkhoic.shop")
                .client(ServiceGenerator.getUnsafeOkHttpClient().build()) // https 처리
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerRequestApi loginService = retrofit.create(ServerRequestApi.class);
        // 알맞는 request 형식 (여기서는 token) 을 파라미터로 담아서 리퀘스트
        loginService.login(token).enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    UserRes user = response.body();
                    Log.d(TAG, "onResponse Success : " + user.toString());
                    String jwt = user.getData().get(0).getToken();
                    PreferenceManager.setString(context, "token", jwt);
                    Log.d(TAG, "onResponse: " + PreferenceManager.getString(context, "token"));
                    ServiceGenerator.createService(ServerRequestApi.class, jwt);
                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d("onResponse Fail : ", response.message());
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());

            }
        });
        /////////////////////////////////////////////////////////////////////////
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        final String TAG = "olaaai";

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String token = account.getIdToken();
            send(account);

            if (account != null) {
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();

                Log.d(TAG, "handleSignInResult:personName " + personName);
                Log.d(TAG, "handleSignInResult:personGivenName " + personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail " + personEmail);
                Log.d(TAG, "handleSignInResult:personId " + personId);
                Log.d(TAG, "handleSignInResult:personFamilyName " + personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto " + personPhoto);
            }

        } catch (ApiException e) {
            Log.w("is", "handleSignInResult:error", e);

        }
    }

}