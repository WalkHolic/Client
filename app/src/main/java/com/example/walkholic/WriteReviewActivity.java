package com.example.walkholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReviewActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    private EditText reviewEdit;
    private Button reg_button;
    private RatingBar reviewRating;
    private TextView titleText;

    // 리퀘스트 사용 변수
    private Uri imageUri;
    private ImageView reviewImageview;
    private ReviewRes reviewRes = new ReviewRes();
    private int kind;
    private int id = -1;
    private String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writeriview);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        editor = preferences.edit();
//        원래는 리뷰 액티비티에서 해당 park의 id값을 넘겨줘야함
        //kind = getIntent().getStringExtra("kind");
        kind = preferences.getInt("objectType", 1);
        id = getIntent().getIntExtra("ID", id);
        name = getIntent().getStringExtra("name");

// 컴포넌트 초기화
        titleText = findViewById(R.id.titleText);
        titleText.setText(name);
        reviewEdit = findViewById(R.id.reviewEdit);
        reviewRating = findViewById(R.id.reviewRating);
        reg_button = findViewById(R.id.reg_button);
        reviewImageview = findViewById(R.id.reviewImageview);

        // 툴바 생성
        Toolbar toolbar = findViewById(R.id.next_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 제거
// 버튼 이벤트 추가
        reg_button.setOnClickListener(view -> {
        // 리뷰 등록 함수
            // 사진
            File realFile = null;
            MultipartBody.Part thumbnail = null;
            if (imageUri != null) {
                String realImagePath = getRealPathFromUri(imageUri);
                realFile = new File(realImagePath);
                Log.d("dlgochan", "realImagePath: " + realImagePath);
                Log.d("dlgochan", "fileName: " + realFile.getName());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), realFile);
                thumbnail = MultipartBody.Part.createFormData("thumbnail", realFile.getName(), requestFile);
            }
            // 리뷰
            ReviewRequestDto tmp = new ReviewRequestDto();
            tmp.setContent(reviewEdit.getText().toString());
            tmp.setScore((double)reviewRating.getRating());
            Gson gson = new Gson();
            String stringDto = gson.toJson(tmp);
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), stringDto);

            //업로드
            switch (kind){
                case 1:
                    uploadParkReview(id, requestBody1, thumbnail);
                    break;
                case 2:
                    uploadRoadReview(id, requestBody1, thumbnail);
                    break;
                case 3:
                    uploadUserRoadReview(id, requestBody1, thumbnail);
                    break;
            }
        });

        // 사진 등록 버튼
        reviewImageview.setOnClickListener(view -> {
            imageUri = null;
            DialogInterface.OnClickListener albumListner = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };
            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setNeutralButton("앨범선택", albumListner)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        });
    }

    private String getRealPathFromUri(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + "=" + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // 앨범 선택
    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        Log.d("dlgochan", "result: " + result);
                        imageUri = result;
                        reviewImageview.setImageURI(result);
                    }
                }
            });

    public void doTakeAlbumAction() {
        getContent.launch("image/*");
    }

    public void uploadParkReview(int id, RequestBody reviewRequestDto, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.uploadParkReview(id, reviewRequestDto, file).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + reviewRes.toString());
                    Toast.makeText(WriteReviewActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                    Toast.makeText(WriteReviewActivity.this, "request error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
                Toast.makeText(WriteReviewActivity.this, "request fail", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void uploadRoadReview(int id, RequestBody reviewRequestDto, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.uploadRoadReview(id, reviewRequestDto, file).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + reviewRes.toString());
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
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public void uploadUserRoadReview(int id, RequestBody reviewRequestDto, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.uploadUserRoadReview(id, reviewRequestDto, file).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + reviewRes.toString());
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
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

